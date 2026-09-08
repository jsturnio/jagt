import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { EntitySelectorComponent, EntitySelectorOption } from 'app/shared/primeng/entity-selector/entity-selector.component';
import { IOrden } from '../../orden/orden.model';
import { OrdenService } from '../../orden/service/orden.service';
import { IPrestacion } from '../../prestacion/prestacion.model';
import { PrestacionService } from '../../prestacion/service/prestacion.service';

@Component({
  selector: 'jhi-practica-ag-filter',
  templateUrl: './practica-ag-filter.html',
  imports: [EntitySelectorComponent],
})
export class PracticaAgFilter implements OnInit {
  @Output() readonly filtersChange = new EventEmitter<Record<string, string[]>>();
  readonly prestacionOptions = signal<EntitySelectorOption<string | number>[]>([]);
  protected readonly prestacionService = inject(PrestacionService);
  readonly ordenOptions = signal<EntitySelectorOption<string | number>[]>([]);
  protected readonly ordenService = inject(OrdenService);

  ngOnInit(): void {
    this.loadPrestacionOptions();
    this.loadOrdenOptions();
  }

  protected loadPrestacionOptions(): void {
    const request = this.prestacionService.query({ size: 10000 }) as Observable<HttpResponse<IPrestacion[]>>;
    request.subscribe(response => {
      const options: EntitySelectorOption<string | number>[] = (response.body ?? []).map((related: IPrestacion) => ({
        label: String(related.codigo ?? related.id),
        value: related.id as string | number,
      }));
      this.prestacionOptions.set([...new Map(options.map(option => [option.value, option])).values()]);
    });
  }

  onPrestacionChange(value: string | number | (string | number)[] | null): void {
    const values = Array.isArray(value) ? value : value == null ? [] : [value];
    const labels = values
      .map(selected => this.prestacionOptions().find(option => String(option.value) === String(selected))?.label)
      .filter((label): label is string => label != null);
    this.filtersChange.emit({ '0.contains': labels });
  }
  protected loadOrdenOptions(): void {
    const request = this.ordenService.query({ size: 10000 }) as Observable<HttpResponse<IOrden[]>>;
    request.subscribe(response => {
      const options: EntitySelectorOption<string | number>[] = (response.body ?? []).map((related: IOrden) => ({
        label: String(related.id ?? related.id),
        value: related.id as string | number,
      }));
      this.ordenOptions.set([...new Map(options.map(option => [option.value, option])).values()]);
    });
  }

  onOrdenChange(value: string | number | (string | number)[] | null): void {
    const values = Array.isArray(value) ? value : value == null ? [] : [value];
    const labels = values
      .map(selected => this.ordenOptions().find(option => String(option.value) === String(selected))?.label)
      .filter((label): label is string => label != null);
    this.filtersChange.emit({ '1.contains': labels });
  }
}
