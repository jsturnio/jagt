import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { EstadoSocio } from 'app/entities/enumerations/estado-socio.model';
import { Gender } from 'app/entities/enumerations/gender.model';
import { TipoIva } from 'app/entities/enumerations/tipo-iva.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert';
import { IBioquimico } from '../bioquimico.model';
import { BioquimicoService } from '../service/bioquimico.service';

import { BioquimicoFormGroup, BioquimicoFormService } from './bioquimico-form.service';

@Component({
  selector: 'jhi-bioquimico-update',
  templateUrl: './bioquimico-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class BioquimicoUpdate implements OnInit {
  readonly isSaving = signal(false);
  bioquimico: IBioquimico | null = null;
  tipoIvaValues = Object.keys(TipoIva);
  estadoSocioValues = Object.keys(EstadoSocio);
  genderValues = Object.keys(Gender);

  usersSharedCollection = signal<IUser[]>([]);

  protected bioquimicoService = inject(BioquimicoService);
  protected bioquimicoFormService = inject(BioquimicoFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BioquimicoFormGroup = this.bioquimicoFormService.createBioquimicoFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bioquimico }) => {
      this.bioquimico = bioquimico;
      if (bioquimico) {
        this.updateForm(bioquimico);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const bioquimico = this.bioquimicoFormService.getBioquimico(this.editForm);
    if (bioquimico.id === null) {
      this.subscribeToSaveResponse(this.bioquimicoService.create(bioquimico));
    } else {
      this.subscribeToSaveResponse(this.bioquimicoService.update(bioquimico));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IBioquimico | null>): void {
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

  protected updateForm(bioquimico: IBioquimico): void {
    this.bioquimico = bioquimico;
    this.bioquimicoFormService.resetForm(this.editForm, bioquimico);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, bioquimico.user));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.bioquimico?.user)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
