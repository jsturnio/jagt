import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IPaquete } from '../paquete.model';

@Component({
  selector: 'jhi-paquete-detail',
  templateUrl: './paquete-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink, FormatMediumDatePipe],
})
export class PaqueteDetail {
  readonly paquete = input<IPaquete | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
