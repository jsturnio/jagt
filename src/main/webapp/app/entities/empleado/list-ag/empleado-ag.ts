import { HttpHeaders } from '@angular/common/http';
import { Component, OnDestroy, OnInit, effect, inject, signal, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { AgGridAngular } from 'ag-grid-angular';
import { AllCommunityModule, type CellValueChangedEvent, ColDef, ModuleRegistry, themeBalham } from 'ag-grid-community';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { AG_GRID_LOCALE_ES } from 'app/shared/ag-grid/ag-grid-locale';
import { AgGridPaginationComponent } from 'app/shared/ag-grid/ag-grid-pagination/ag-grid-pagination.component';
import { buildGridFilterParams } from 'app/shared/ag-grid/ag-grid-utils';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
import { Filter, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { EmpleadoDeleteDialog } from '../delete/empleado-delete-dialog';
import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';

import { EmpleadoAgFilter } from './empleado-ag-filter';
import { getEmpleadoColumnDefs } from './empleado-grid.config';

ModuleRegistry.registerModules([AllCommunityModule]);

@Component({
  selector: 'jhi-empleado-ag',
  templateUrl: './empleado-ag.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    Filter,
    AgGridPaginationComponent,
    AgGridAngular,
    EmpleadoAgFilter,
  ],
})
export class EmpleadoAg implements OnInit, OnDestroy {
  public tema = themeBalham;

  subscription: Subscription | null = null;
  readonly empleados = signal<IEmpleado[]>([]);

  readonly localeText = AG_GRID_LOCALE_ES;
  readonly gridFilter = signal<Record<string, string | number | boolean>>({});
  readonly aboveFilters = signal<Record<string, string[]>>({});
  readonly routeInitialized = signal(false);
  columnDefs: ColDef[] = getEmpleadoColumnDefs(this.actionsCellRenderer.bind(this));

  defaultColDef: ColDef = {
    sortable: true,
    filter: true,
    resizable: true,
    editable: true,
  };

  onCellValueChanged(event: CellValueChangedEvent): void {
    const empleado = event.data as IEmpleado;
    this.empleadoService.update(empleado).subscribe(() => {
      this.load();
    });
  }

  onFilterChanged(event: any): void {
    const model = event.api.getFilterModel();
    this.gridFilter.set(buildGridFilterParams(model));
    this.page.set(1);
    this.load();
  }

  onAboveFiltersChange(filters: Record<string, string[]>): void {
    this.aboveFilters.update(current => ({ ...current, ...filters }));
    this.handleNavigation(1, this.sortState(), this.filters.filterOptions);
  }

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly totalItems = signal(0);
  readonly page = signal(1);

  readonly router = inject(Router);
  protected readonly empleadoService = inject(EmpleadoService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.empleadoService.empleadosResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected readonly filterOptions = toSignal(this.filters.filterChanges);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      const headers = this.empleadoService.empleadosResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.empleados.set(this.fillComponentAttributesFromResponseBody([...this.empleadoService.empleados()]));
    });

    effect(() => {
      const filterOptions = this.filterOptions();
      if (filterOptions && this.routeInitialized()) {
        untracked(() => {
          // Only watch for filter changes. Other signals should be ignored.
          this.handleNavigation(1, this.sortState(), filterOptions);
        });
      }
    });

    (window as any).deleteEmpleado = this.deleteEmpleadoGlobal;
  }

  ngOnDestroy(): void {
    delete (window as any).deleteEmpleado;
  }

  trackId = (item: IEmpleado): number => this.empleadoService.getEmpleadoIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  // Función global para eliminar desde el grid
  private deleteEmpleadoGlobal = (id: number) => {
    const empleado = this.empleados().find(o => o.id === id);
    if (empleado) {
      this.delete(empleado);
    }
  };

  delete(empleado: IEmpleado): void {
    const modalRef = this.modalService.open(EmpleadoDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.empleado = empleado;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  actionsCellRenderer(params: any): string {
    const empleado = params.data;
    return `
      <div class="d-flex gap-0">
        <a href="/empleado/${empleado.id}/view" class="btn  btn-sm icon-button icon-view" title="Ver" aria-label="Ver"></a>
        <a href="/empleado/${empleado.id}/edit" class="btn  btn-sm icon-button icon-edit" title="Editar"></a>
        <button class="btn  btn-sm icon-button icon-delete" title="Eliminar" onclick="window.deleteEmpleado(${empleado.id})"></button>
      </div>
    `;
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page(), event, this.filters.filterOptions);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState(), this.filters.filterOptions);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page.set(+(page ?? 1));
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
    this.filters.initializeFromParams(params);
    this.aboveFilters.update(filters => ({
      ...filters,
      '0.contains': params
        .getAll('0.contains')
        .flatMap(value => value.split(','))
        .filter(Boolean),
    }));
    this.routeInitialized.set(true);
  }

  protected fillComponentAttributesFromResponseBody(data: IEmpleado[]): IEmpleado[] {
    return data;
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER)));
  }

  protected queryBackend(): void {
    const pageToLoad: number = this.page();
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage(),
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    for (const filterOption of this.filters.filterOptions) {
      queryObject[filterOption.name] = filterOption.values;
    }
    Object.assign(queryObject, this.gridFilter());
    Object.assign(queryObject, this.aboveFilters());
    this.empleadoService.empleadosParams.set(queryObject);
  }

  protected handleNavigation(page: number, sortState: SortState, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage(),
      sort: this.sortService.buildSortParam(sortState),
    };

    if (filterOptions) {
      for (const filterOption of filterOptions) {
        queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
      }
    }
    Object.assign(queryParamsObj, this.gridFilter());
    Object.assign(queryParamsObj, this.aboveFilters());

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
