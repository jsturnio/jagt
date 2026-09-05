import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { INomenclador } from '../nomenclador.model';
import { NomencladorService } from '../service/nomenclador.service';

@Component({
  templateUrl: './nomenclador-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class NomencladorDeleteDialog {
  nomenclador?: INomenclador;

  protected readonly nomencladorService = inject(NomencladorService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nomencladorService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
