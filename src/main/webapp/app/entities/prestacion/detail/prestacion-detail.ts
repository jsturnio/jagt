import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbTooltip } from '@ng-bootstrap/ng-bootstrap/tooltip';

import { Alert, AlertError } from 'app/shared/alert';
import { IPrestacion } from '../prestacion.model';

@Component({
  selector: 'jhi-prestacion-detail',
  templateUrl: './prestacion-detail.html',
  imports: [FontAwesomeModule, NgbTooltip, Alert, AlertError, RouterLink],
})
export class PrestacionDetail {
  readonly prestacion = input<IPrestacion | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
