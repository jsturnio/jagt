import { HttpHeaders } from '@angular/common/http';
import { Component, OnDestroy, OnInit, effect, inject, signal, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap/pagination';
import { AgGridAngular } from 'ag-grid-angular';
import { AllCommunityModule, type CellValueChangedEvent, ColDef, ModuleRegistry, themeBalham } from 'ag-grid-community';
import { Subscription, combineLatest, filter, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { AG_GRID_LOCALE_ES } from 'app/shared/ag-grid/ag-grid-locale';
import { buildGridFilterParams } from 'app/shared/ag-grid/ag-grid-utils';
import { Alert } from 'app/shared/alert/alert';
import { AlertError } from 'app/shared/alert/alert-error';
//import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { Filter, FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter';
import { ItemCount } from 'app/shared/pagination';
import { SortService, type SortState, sortStateSignal } from 'app/shared/sort';
//import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';

import { PaqueteDeleteDialog } from '../delete/paquete-delete-dialog';
import { IPaquete } from '../paquete.model';
import { PaqueteService } from '../service/paquete.service';

import { getPaqueteColumnDefs } from './paquete-grid.config';

ModuleRegistry.registerModules([AllCommunityModule]);

@Component({
  selector: 'jhi-paquete-ag',
  templateUrl: './paquete-ag.html',
  imports: [
    RouterLink,
    FormsModule,
    FontAwesomeModule,
    AlertError,
    Alert,
    //    SortDirective,
    //    SortByDirective,
    //    FormatMediumDatetimePipe,
    //    FormatMediumDatePipe,
    Filter,
    NgbPagination,
    ItemCount,
    AgGridAngular,
  ],
})
export class PaqueteAg implements OnInit, OnDestroy {
  public tema = themeBalham;

  subscription: Subscription | null = null;
  readonly paquetes = signal<IPaquete[]>([]);

  readonly localeText = AG_GRID_LOCALE_ES;
  readonly gridFilter = signal<Record<string, string | number | boolean>>({});

  columnDefs: ColDef[] = getPaqueteColumnDefs(this.actionsCellRenderer.bind(this));

  defaultColDef: ColDef = {
    sortable: true,
    filter: true,
    resizable: true,
    editable: true,
  };

  onCellValueChanged(event: CellValueChangedEvent): void {
    const paquete = event.data as IPaquete;
    this.paqueteService.update(paquete).subscribe(() => {
      this.load();
    });
  }

  onFilterChanged(event: any): void {
    const model = event.api.getFilterModel();
    this.gridFilter.set(buildGridFilterParams(model));
    this.page.set(1);
    this.load();
  }

  sortState = sortStateSignal({});
  filters: IFilterOptions = new FilterOptions();

  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly totalItems = signal(0);
  readonly page = signal(1);

  readonly router = inject(Router);
  protected readonly paqueteService = inject(PaqueteService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.paqueteService.paquetesResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected readonly filterOptions = toSignal(this.filters.filterChanges);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      const headers = this.paqueteService.paquetesResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.paquetes.set(this.fillComponentAttributesFromResponseBody([...this.paqueteService.paquetes()]));
    });

    effect(() => {
      const filterOptions = this.filterOptions();
      if (filterOptions) {
        untracked(() => {
          // Only watch for filter changes. Other signals should be ignored.
          this.handleNavigation(1, this.sortState(), filterOptions);
        });
      }
    });

    (window as any).deletePaquete = this.deletePaqueteGlobal;
  }

  ngOnDestroy(): void {
    delete (window as any).deletePaquete;
  }

  trackId = (item: IPaquete): number => this.paqueteService.getPaqueteIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  // Función global para eliminar desde el grid
  private deletePaqueteGlobal = (id: number) => {
    const paquete = this.paquetes().find(o => o.id === id);
    if (paquete) {
      this.delete(paquete);
    }
  };

  delete(paquete: IPaquete): void {
    const modalRef = this.modalService.open(PaqueteDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paquete = paquete;
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
    const paquete = params.data;
    return `
      <div class="d-flex gap-0">
        <a href="/paquete/${paquete.id}/view" class="btn  btn-sm icon-button icon-view" title="Ver" aria-label="Ver"></a>
        <a href="/paquete/${paquete.id}/edit" class="btn  btn-sm icon-button icon-edit" title="Editar"></a>
        <button class="btn  btn-sm icon-button icon-delete" title="Eliminar" onclick="window.deletePaquete(${paquete.id})"></button>
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
  }

  protected fillComponentAttributesFromResponseBody(data: IPaquete[]): IPaquete[] {
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
    this.paqueteService.paquetesParams.set(queryObject);
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

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
