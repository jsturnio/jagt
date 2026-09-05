import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { IPractica } from '../practica.model';
import { PracticaService } from '../service/practica.service';

@Component({
  templateUrl: './practica-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class PracticaDeleteDialog {
  practica?: IPractica;

  protected readonly practicaService = inject(PracticaService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.practicaService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
