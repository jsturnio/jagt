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
import { NomencladorDeleteDialog } from '../delete/nomenclador-delete-dialog';
import { INomenclador } from '../nomenclador.model';
import { NomencladorService } from '../service/nomenclador.service';

import { getNomencladorColumnDefs } from './nomenclador-grid.config';

ModuleRegistry.registerModules([AllCommunityModule]);

@Component({
  selector: 'jhi-nomenclador-ag',
  templateUrl: './nomenclador-ag.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, Filter, AgGridPaginationComponent, AgGridAngular],
})
export class NomencladorAg implements OnInit, OnDestroy {
  public tema = themeBalham;

  subscription: Subscription | null = null;
  readonly nomencladors = signal<INomenclador[]>([]);

  readonly localeText = AG_GRID_LOCALE_ES;
  readonly gridFilter = signal<Record<string, string | number | boolean>>({});

  columnDefs: ColDef[] = getNomencladorColumnDefs(this.actionsCellRenderer.bind(this));

  defaultColDef: ColDef = {
    sortable: true,
    filter: true,
    resizable: true,
    editable: true,
  };

  onCellValueChanged(event: CellValueChangedEvent): void {
    const nomenclador = event.data as INomenclador;
    this.nomencladorService.update(nomenclador).subscribe(() => {
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
  protected readonly nomencladorService = inject(NomencladorService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.nomencladorService.nomencladorsResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected readonly filterOptions = toSignal(this.filters.filterChanges);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      const headers = this.nomencladorService.nomencladorsResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.nomencladors.set(this.fillComponentAttributesFromResponseBody([...this.nomencladorService.nomencladors()]));
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

    (window as any).deleteNomenclador = this.deleteNomencladorGlobal;
  }

  ngOnDestroy(): void {
    delete (window as any).deleteNomenclador;
  }

  trackId = (item: INomenclador): number => this.nomencladorService.getNomencladorIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  // Función global para eliminar desde el grid
  private deleteNomencladorGlobal = (id: number) => {
    const nomenclador = this.nomencladors().find(o => o.id === id);
    if (nomenclador) {
      this.delete(nomenclador);
    }
  };

  delete(nomenclador: INomenclador): void {
    const modalRef = this.modalService.open(NomencladorDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.nomenclador = nomenclador;
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
    const nomenclador = params.data;
    return `
      <div class="d-flex gap-0">
        <a href="/nomenclador/${nomenclador.id}/view" class="btn  btn-sm icon-button icon-view" title="Ver" aria-label="Ver"></a>
        <a href="/nomenclador/${nomenclador.id}/edit" class="btn  btn-sm icon-button icon-edit" title="Editar"></a>
        <button class="btn  btn-sm icon-button icon-delete" title="Eliminar" onclick="window.deleteNomenclador(${nomenclador.id})"></button>
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

  protected fillComponentAttributesFromResponseBody(data: INomenclador[]): INomenclador[] {
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
    this.nomencladorService.nomencladorsParams.set(queryObject);
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
