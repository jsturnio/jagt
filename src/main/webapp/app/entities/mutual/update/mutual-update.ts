import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { TipoIva } from 'app/entities/enumerations/tipo-iva.model';
import { AlertError } from 'app/shared/alert';
import { IMutual } from '../mutual.model';
import { MutualService } from '../service/mutual.service';

import { MutualFormGroup, MutualFormService } from './mutual-form.service';

@Component({
  selector: 'jhi-mutual-update',
  templateUrl: './mutual-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class MutualUpdate implements OnInit {
  readonly isSaving = signal(false);
  mutual: IMutual | null = null;
  tipoIvaValues = Object.keys(TipoIva);

  protected mutualService = inject(MutualService);
  protected mutualFormService = inject(MutualFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MutualFormGroup = this.mutualFormService.createMutualFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mutual }) => {
      this.mutual = mutual;
      if (mutual) {
        this.updateForm(mutual);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const mutual = this.mutualFormService.getMutual(this.editForm);
    if (mutual.id === null) {
      this.subscribeToSaveResponse(this.mutualService.create(mutual));
    } else {
      this.subscribeToSaveResponse(this.mutualService.update(mutual));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IMutual | null>): void {
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

  protected updateForm(mutual: IMutual): void {
    this.mutual = mutual;
    this.mutualFormService.resetForm(this.editForm, mutual);
  }
}
