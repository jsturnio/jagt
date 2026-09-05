import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { IMutual } from '../mutual.model';

@Component({
  selector: 'jhi-mutual-detail',
  templateUrl: './mutual-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink],
})
export class MutualDetail {
  readonly mutual = input<IMutual | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
