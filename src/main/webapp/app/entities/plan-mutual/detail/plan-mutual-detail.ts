import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { IPlanMutual } from '../plan-mutual.model';

@Component({
  selector: 'jhi-plan-mutual-detail',
  templateUrl: './plan-mutual-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class PlanMutualDetail {
  readonly planMutual = input<IPlanMutual | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
