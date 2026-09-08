import { Component, OnInit, effect, inject, signal } from '@angular/core';

import { AgGridAngular } from 'ag-grid-angular';
import { AllCommunityModule, ColDef, ModuleRegistry, themeBalham } from 'ag-grid-community';

import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { AgGridPaginationComponent } from 'app/shared/ag-grid/ag-grid-pagination/ag-grid-pagination.component';
import { getOrdenColumnDefs } from '../../orden/list-ag/orden-grid.config';
import { IOrden } from '../../orden/orden.model';
import { OrdenService } from '../../orden/service/orden.service';
import { getPracticaColumnDefs } from '../../practica/list-ag/practica-grid.config';
import { IPractica } from '../../practica/practica.model';
import { PracticaService } from '../../practica/service/practica.service';

ModuleRegistry.registerModules([AllCommunityModule]);

@Component({
  selector: 'jhi-orden-master-detail',
  templateUrl: './orden-practica-detail.html',
  imports: [AgGridAngular, AgGridPaginationComponent],
})
export class OrdenMasterDetail implements OnInit {
  readonly theme = themeBalham;

  readonly ordenPage = signal(1);
  readonly ordenItemsPerPage = signal(ITEMS_PER_PAGE);
  readonly ordenTotalItems = signal(0);

  readonly practicaPage = signal(1);
  readonly practicaItemsPerPage = signal(ITEMS_PER_PAGE);
  readonly practicaTotalItems = signal(0);

  readonly ordens = signal<IOrden[]>([]);
  readonly ordenColumnDefs: ColDef[] = getOrdenColumnDefs(() => '');
  private readonly ordenService = inject(OrdenService);
  readonly practicas = signal<IPractica[]>([]);
  readonly practicaColumnDefs: ColDef[] = getPracticaColumnDefs(() => '');
  private readonly practicaService = inject(PracticaService);

  readonly selected0 = signal<IOrden | undefined>(undefined);

  constructor() {
    effect(() => {
      const rows = [...this.ordenService.ordens()];
      this.ordens.set(rows);
    });
    effect(() => {
      const rows = [...this.practicaService.practicas()];
      const parentId = this.selected0()?.id;
      this.practicas.set(parentId == null ? [] : rows.filter(row => row.orden?.id === parentId));
    });

    effect(() => {
      const { headers } = this.ordenService.ordensResource;

      this.ordenTotalItems.set(Number(headers()?.get(TOTAL_COUNT_RESPONSE_HEADER) ?? 0));
    });

    effect(() => {
      const { headers } = this.practicaService.practicasResource;

      this.practicaTotalItems.set(Number(headers()?.get(TOTAL_COUNT_RESPONSE_HEADER) ?? 0));
    });
  }

  ngOnInit(): void {
    this.loadOrden();
  }

  onSelectionChangedOrden(event: any): void {
    const selected = event.api.getSelectedRows()[0] as IOrden | undefined;
    this.selected0.set(selected);
    this.practicaPage.set(1);
    this.practicas.set([]);
    if (selected?.id != null) {
      this.loadPractica(selected.id);
    }
  }

  onSelectionChangedPractica(event: any): void {
    const selected = event.api.getSelectedRows()[0] as IPractica | undefined;
    this.selected0.set(selected);
  }

  navigateToOrdenPage(page: number): void {
    this.ordenPage.set(page);
    this.loadOrden();
  }
  navigateToPracticaPage(page: number): void {
    this.practicaPage.set(page);

    const parentId = this.selected0()?.id;
    if (parentId != null) {
      this.loadPractica(parentId);
    }
  }

  private loadOrden(): void {
    this.ordenService.ordensParams.set({
      page: this.ordenPage() - 1,
      size: this.ordenItemsPerPage(),
      sort: 'id,asc',
    });
  }

  private loadPractica(parentId: number): void {
    this.practicaService.practicasParams.set({
      page: this.practicaPage() - 1,
      size: this.practicaItemsPerPage(),
      sort: 'id,asc',
      'ordenId.equals': parentId,
    });
  }
}
