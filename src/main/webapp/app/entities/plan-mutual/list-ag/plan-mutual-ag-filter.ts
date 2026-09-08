import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { EntitySelectorComponent, EntitySelectorOption } from 'app/shared/primeng/entity-selector/entity-selector.component';
import { IMutual } from '../../mutual/mutual.model';
import { MutualService } from '../../mutual/service/mutual.service';

@Component({
  selector: 'jhi-plan-mutual-ag-filter',
  templateUrl: './plan-mutual-ag-filter.html',
  imports: [EntitySelectorComponent],
})
export class PlanMutualAgFilter implements OnInit {
  @Output() readonly filtersChange = new EventEmitter<Record<string, string[]>>();
  readonly mutualOptions = signal<EntitySelectorOption<string | number>[]>([]);
  protected readonly mutualService = inject(MutualService);

  ngOnInit(): void {
    this.loadMutualOptions();
  }

  protected loadMutualOptions(): void {
    const request = this.mutualService.query({ size: 10000 }) as Observable<HttpResponse<IMutual[]>>;
    request.subscribe(response => {
      const options: EntitySelectorOption<string | number>[] = (response.body ?? []).map((related: IMutual) => ({
        label: String(related.nombre ?? related.id),
        value: related.id as string | number,
      }));
      this.mutualOptions.set([...new Map(options.map(option => [option.value, option])).values()]);
    });
  }

  onMutualChange(value: string | number | (string | number)[] | null): void {
    const values = Array.isArray(value) ? value : value == null ? [] : [value];
    const labels = values
      .map(selected => this.mutualOptions().find(option => String(option.value) === String(selected))?.label)
      .filter((label): label is string => label != null);
    this.filtersChange.emit({ '0.contains': labels });
  }
}
