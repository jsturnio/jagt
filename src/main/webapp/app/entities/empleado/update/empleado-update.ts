import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { Gender } from 'app/entities/enumerations/gender.model';
import { TipoIva } from 'app/entities/enumerations/tipo-iva.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert';
import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';

import { EmpleadoFormGroup, EmpleadoFormService } from './empleado-form.service';

@Component({
  selector: 'jhi-empleado-update',
  templateUrl: './empleado-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class EmpleadoUpdate implements OnInit {
  readonly isSaving = signal(false);
  empleado: IEmpleado | null = null;
  tipoIvaValues = Object.keys(TipoIva);
  genderValues = Object.keys(Gender);

  usersSharedCollection = signal<IUser[]>([]);

  protected empleadoService = inject(EmpleadoService);
  protected empleadoFormService = inject(EmpleadoFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmpleadoFormGroup = this.empleadoFormService.createEmpleadoFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleado }) => {
      this.empleado = empleado;
      if (empleado) {
        this.updateForm(empleado);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const empleado = this.empleadoFormService.getEmpleado(this.editForm);
    if (empleado.id === null) {
      this.subscribeToSaveResponse(this.empleadoService.create(empleado));
    } else {
      this.subscribeToSaveResponse(this.empleadoService.update(empleado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEmpleado | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(empleado: IEmpleado): void {
    this.empleado = empleado;
    this.empleadoFormService.resetForm(this.editForm, empleado);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, empleado.usuario));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.empleado?.usuario)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
