import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { IPlanMutual } from '../plan-mutual.model';
import { PlanMutualService } from '../service/plan-mutual.service';

@Component({
  templateUrl: './plan-mutual-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class PlanMutualDeleteDialog {
  planMutual?: IPlanMutual;

  protected readonly planMutualService = inject(PlanMutualService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.planMutualService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
