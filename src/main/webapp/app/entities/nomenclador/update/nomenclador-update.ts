import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { TipoNomenclador } from 'app/entities/enumerations/tipo-nomenclador.model';
import { IMutual } from 'app/entities/mutual/mutual.model';
import { MutualService } from 'app/entities/mutual/service/mutual.service';
import { AlertError } from 'app/shared/alert';
import { INomenclador } from '../nomenclador.model';
import { NomencladorService } from '../service/nomenclador.service';

import { NomencladorFormGroup, NomencladorFormService } from './nomenclador-form.service';

@Component({
  selector: 'jhi-nomenclador-update',
  templateUrl: './nomenclador-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class NomencladorUpdate implements OnInit {
  readonly isSaving = signal(false);
  nomenclador: INomenclador | null = null;
  tipoNomencladorValues = Object.keys(TipoNomenclador);

  mutualsSharedCollection = signal<IMutual[]>([]);

  protected nomencladorService = inject(NomencladorService);
  protected nomencladorFormService = inject(NomencladorFormService);
  protected mutualService = inject(MutualService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NomencladorFormGroup = this.nomencladorFormService.createNomencladorFormGroup();

  compareMutual = (o1: IMutual | null, o2: IMutual | null): boolean => this.mutualService.compareMutual(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nomenclador }) => {
      this.nomenclador = nomenclador;
      if (nomenclador) {
        this.updateForm(nomenclador);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const nomenclador = this.nomencladorFormService.getNomenclador(this.editForm);
    if (nomenclador.id === null) {
      this.subscribeToSaveResponse(this.nomencladorService.create(nomenclador));
    } else {
      this.subscribeToSaveResponse(this.nomencladorService.update(nomenclador));
    }
  }

  protected subscribeToSaveResponse(result: Observable<INomenclador | null>): void {
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

  protected updateForm(nomenclador: INomenclador): void {
    this.nomenclador = nomenclador;
    this.nomencladorFormService.resetForm(this.editForm, nomenclador);

    this.mutualsSharedCollection.update(mutuals => this.mutualService.addMutualToCollectionIfMissing<IMutual>(mutuals, nomenclador.mutual));
  }

  protected loadRelationshipsOptions(): void {
    this.mutualService
      .query()
      .pipe(map((res: HttpResponse<IMutual[]>) => res.body ?? []))
      .pipe(map((mutuals: IMutual[]) => this.mutualService.addMutualToCollectionIfMissing<IMutual>(mutuals, this.nomenclador?.mutual)))
      .subscribe((mutuals: IMutual[]) => this.mutualsSharedCollection.set(mutuals));
  }
}
