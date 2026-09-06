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
import { IBioquimico } from '../bioquimico.model';
import { BioquimicoDeleteDialog } from '../delete/bioquimico-delete-dialog';
import { BioquimicoService } from '../service/bioquimico.service';

import { getBioquimicoColumnDefs } from './bioquimico-grid.config';

ModuleRegistry.registerModules([AllCommunityModule]);

@Component({
  selector: 'jhi-bioquimico-ag',
  templateUrl: './bioquimico-ag.html',
  imports: [RouterLink, FormsModule, FontAwesomeModule, AlertError, Alert, Filter, AgGridPaginationComponent, AgGridAngular],
})
export class BioquimicoAg implements OnInit, OnDestroy {
  public tema = themeBalham;

  subscription: Subscription | null = null;
  readonly bioquimicos = signal<IBioquimico[]>([]);

  readonly localeText = AG_GRID_LOCALE_ES;
  readonly gridFilter = signal<Record<string, string | number | boolean>>({});

  columnDefs: ColDef[] = getBioquimicoColumnDefs(this.actionsCellRenderer.bind(this));

  defaultColDef: ColDef = {
    sortable: true,
    filter: true,
    resizable: true,
    editable: true,
  };

  onCellValueChanged(event: CellValueChangedEvent): void {
    const bioquimico = event.data as IBioquimico;
    this.bioquimicoService.update(bioquimico).subscribe(() => {
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
  protected readonly bioquimicoService = inject(BioquimicoService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.bioquimicoService.bioquimicosResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected readonly filterOptions = toSignal(this.filters.filterChanges);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      const headers = this.bioquimicoService.bioquimicosResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.bioquimicos.set(this.fillComponentAttributesFromResponseBody([...this.bioquimicoService.bioquimicos()]));
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

    (window as any).deleteBioquimico = this.deleteBioquimicoGlobal;
  }

  ngOnDestroy(): void {
    delete (window as any).deleteBioquimico;
  }

  trackId = (item: IBioquimico): number => this.bioquimicoService.getBioquimicoIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  // Función global para eliminar desde el grid
  private deleteBioquimicoGlobal = (id: number) => {
    const bioquimico = this.bioquimicos().find(o => o.id === id);
    if (bioquimico) {
      this.delete(bioquimico);
    }
  };

  delete(bioquimico: IBioquimico): void {
    const modalRef = this.modalService.open(BioquimicoDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bioquimico = bioquimico;
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
    const bioquimico = params.data;
    return `
      <div class="d-flex gap-0">
        <a href="/bioquimico/${bioquimico.id}/view" class="btn  btn-sm icon-button icon-view" title="Ver" aria-label="Ver"></a>
        <a href="/bioquimico/${bioquimico.id}/edit" class="btn  btn-sm icon-button icon-edit" title="Editar"></a>
        <button class="btn  btn-sm icon-button icon-delete" title="Eliminar" onclick="window.deleteBioquimico(${bioquimico.id})"></button>
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

  protected fillComponentAttributesFromResponseBody(data: IBioquimico[]): IBioquimico[] {
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
    this.bioquimicoService.bioquimicosParams.set(queryObject);
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
