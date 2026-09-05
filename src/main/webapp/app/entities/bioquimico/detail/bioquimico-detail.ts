import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IBioquimico } from '../bioquimico.model';

@Component({
  selector: 'jhi-bioquimico-detail',
  templateUrl: './bioquimico-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink, FormatMediumDatePipe],
})
export class BioquimicoDetail {
  readonly bioquimico = input<IBioquimico | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
