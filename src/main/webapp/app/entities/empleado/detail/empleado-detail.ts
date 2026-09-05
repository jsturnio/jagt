import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IEmpleado } from '../empleado.model';

@Component({
  selector: 'jhi-empleado-detail',
  templateUrl: './empleado-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink, FormatMediumDatePipe],
})
export class EmpleadoDetail {
  readonly empleado = input<IEmpleado | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
