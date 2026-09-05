import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IOrden } from '../orden.model';

@Component({
  selector: 'jhi-orden-detail',
  templateUrl: './orden-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class OrdenDetail {
  readonly orden = input<IOrden | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
