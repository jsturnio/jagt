import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { INomenclador } from '../nomenclador.model';

@Component({
  selector: 'jhi-nomenclador-detail',
  templateUrl: './nomenclador-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class NomencladorDetail {
  readonly nomenclador = input<INomenclador | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
