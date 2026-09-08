import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { EntitySelectorComponent, EntitySelectorOption } from 'app/shared/primeng/entity-selector/entity-selector.component';
import { IPlanMutual } from '../../plan-mutual/plan-mutual.model';
import { PlanMutualService } from '../../plan-mutual/service/plan-mutual.service';

@Component({
  selector: 'jhi-paquete-ag-filter',
  templateUrl: './paquete-ag-filter.html',
  imports: [EntitySelectorComponent],
})
export class PaqueteAgFilter implements OnInit {
  @Output() readonly filtersChange = new EventEmitter<Record<string, string[]>>();
  readonly planOptions = signal<EntitySelectorOption<string | number>[]>([]);
  protected readonly planService = inject(PlanMutualService);

  ngOnInit(): void {
    this.loadPlanOptions();
  }

  protected loadPlanOptions(): void {
    const request = this.planService.query({ size: 10000 }) as Observable<HttpResponse<IPlanMutual[]>>;
    request.subscribe(response => {
      const options: EntitySelectorOption<string | number>[] = (response.body ?? []).map((related: IPlanMutual) => ({
        label: String(related.categoria ?? related.id),
        value: related.id as string | number,
      }));
      this.planOptions.set([...new Map(options.map(option => [option.value, option])).values()]);
    });
  }

  onPlanChange(value: string | number | (string | number)[] | null): void {
    const values = Array.isArray(value) ? value : value == null ? [] : [value];
    const labels = values
      .map(selected => this.planOptions().find(option => String(option.value) === String(selected))?.label)
      .filter((label): label is string => label != null);
    this.filtersChange.emit({ '0.contains': labels });
  }
}
