import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { IMutual } from '../mutual.model';
import { MutualService } from '../service/mutual.service';

@Component({
  templateUrl: './mutual-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class MutualDeleteDialog {
  mutual?: IMutual;

  protected readonly mutualService = inject(MutualService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mutualService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
