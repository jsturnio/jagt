import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { IPractica } from '../practica.model';

@Component({
  selector: 'jhi-practica-detail',
  templateUrl: './practica-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class PracticaDetail {
  readonly practica = input<IPractica | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
