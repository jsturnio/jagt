import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';

@Component({
  templateUrl: './empleado-delete-dialog.html',
  imports: [FormsModule, FontAwesomeModule, AlertError],
})
export class EmpleadoDeleteDialog {
  empleado?: IEmpleado;

  protected readonly empleadoService = inject(EmpleadoService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.empleadoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
