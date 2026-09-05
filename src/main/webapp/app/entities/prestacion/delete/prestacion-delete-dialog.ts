import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { IPrestacion } from '../prestacion.model';
import { PrestacionService } from '../service/prestacion.service';

@Component({
  templateUrl: './prestacion-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class PrestacionDeleteDialog {
  prestacion?: IPrestacion;

  protected readonly prestacionService = inject(PrestacionService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prestacionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
