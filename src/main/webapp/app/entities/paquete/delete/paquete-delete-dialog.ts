import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { IPaquete } from '../paquete.model';
import { PaqueteService } from '../service/paquete.service';

@Component({
  templateUrl: './paquete-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class PaqueteDeleteDialog {
  paquete?: IPaquete;

  protected readonly paqueteService = inject(PaqueteService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paqueteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
