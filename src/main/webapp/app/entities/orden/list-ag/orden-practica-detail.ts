import { Component, OnInit, effect, inject, signal } from '@angular/core';

import { AgGridAngular } from 'ag-grid-angular';
import { AllCommunityModule, ColDef, ModuleRegistry, themeBalham } from 'ag-grid-community';

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
  imports: [AgGridAngular],
})
export class OrdenMasterDetail implements OnInit {
  readonly theme = themeBalham;

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
  }

  ngOnInit(): void {
    this.loadOrden();
  }

  onSelectionChangedOrden(event: any): void {
    const selected = event.api.getSelectedRows()[0] as IOrden | undefined;
    this.selected0.set(selected);
    this.practicas.set([]);
    if (selected?.id != null) {
      this.loadPractica(selected.id);
    }
  }

  onSelectionChangedPractica(event: any): void {
    const selected = event.api.getSelectedRows()[0] as IPractica | undefined;
    this.selected0.set(selected);
  }

  private loadOrden(): void {
    this.ordenService.ordensParams.set({ page: 0, size: 1000, sort: 'id,asc' });
  }

  private loadPractica(parentId: number): void {
    this.practicaService.practicasParams.set({
      page: 0,
      size: 1000,
      sort: 'id,asc',
      'ordenId.equals': parentId,
    });
  }
}
