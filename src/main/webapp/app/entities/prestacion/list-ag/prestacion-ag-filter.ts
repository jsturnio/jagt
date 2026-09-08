import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { EntitySelectorComponent, EntitySelectorOption } from 'app/shared/primeng/entity-selector/entity-selector.component';
import { INomenclador } from '../../nomenclador/nomenclador.model';
import { NomencladorService } from '../../nomenclador/service/nomenclador.service';

@Component({
  selector: 'jhi-prestacion-ag-filter',
  templateUrl: './prestacion-ag-filter.html',
  imports: [EntitySelectorComponent],
})
export class PrestacionAgFilter implements OnInit {
  @Output() readonly filtersChange = new EventEmitter<Record<string, string[]>>();
  readonly nomencladorOptions = signal<EntitySelectorOption<string | number>[]>([]);
  protected readonly nomencladorService = inject(NomencladorService);

  ngOnInit(): void {
    this.loadNomencladorOptions();
  }

  protected loadNomencladorOptions(): void {
    const request = this.nomencladorService.query({ size: 10000 }) as Observable<HttpResponse<INomenclador[]>>;
    request.subscribe(response => {
      const options: EntitySelectorOption<string | number>[] = (response.body ?? []).map((related: INomenclador) => ({
        label: String(related.nombre ?? related.id),
        value: related.id as string | number,
      }));
      this.nomencladorOptions.set([...new Map(options.map(option => [option.value, option])).values()]);
    });
  }

  onNomencladorChange(value: string | number | (string | number)[] | null): void {
    const values = Array.isArray(value) ? value : value == null ? [] : [value];
    const labels = values
      .map(selected => this.nomencladorOptions().find(option => String(option.value) === String(selected))?.label)
      .filter((label): label is string => label != null);
    this.filtersChange.emit({ '0.contains': labels });
  }
}
