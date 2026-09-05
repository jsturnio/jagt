import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { IOrden } from '../orden.model';
import { OrdenService } from '../service/orden.service';

@Component({
  templateUrl: './orden-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class OrdenDeleteDialog {
  orden?: IOrden;

  protected readonly ordenService = inject(OrdenService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ordenService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
