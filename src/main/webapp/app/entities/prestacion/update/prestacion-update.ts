import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbTooltip } from '@ng-bootstrap/ng-bootstrap/tooltip';
import { Observable, finalize, map } from 'rxjs';

import { INomenclador } from 'app/entities/nomenclador/nomenclador.model';
import { NomencladorService } from 'app/entities/nomenclador/service/nomenclador.service';
import { AlertError } from 'app/shared/alert';
import { IPrestacion } from '../prestacion.model';
import { PrestacionService } from '../service/prestacion.service';

import { PrestacionFormGroup, PrestacionFormService } from './prestacion-form.service';

@Component({
  selector: 'jhi-prestacion-update',
  templateUrl: './prestacion-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule, NgbTooltip],
})
export class PrestacionUpdate implements OnInit {
  readonly isSaving = signal(false);
  prestacion: IPrestacion | null = null;

  nomencladorsSharedCollection = signal<INomenclador[]>([]);

  protected prestacionService = inject(PrestacionService);
  protected prestacionFormService = inject(PrestacionFormService);
  protected nomencladorService = inject(NomencladorService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PrestacionFormGroup = this.prestacionFormService.createPrestacionFormGroup();

  compareNomenclador = (o1: INomenclador | null, o2: INomenclador | null): boolean => this.nomencladorService.compareNomenclador(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestacion }) => {
      this.prestacion = prestacion;
      if (prestacion) {
        this.updateForm(prestacion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const prestacion = this.prestacionFormService.getPrestacion(this.editForm);
    if (prestacion.id === null) {
      this.subscribeToSaveResponse(this.prestacionService.create(prestacion));
    } else {
      this.subscribeToSaveResponse(this.prestacionService.update(prestacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPrestacion | null>): void {
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

  protected updateForm(prestacion: IPrestacion): void {
    this.prestacion = prestacion;
    this.prestacionFormService.resetForm(this.editForm, prestacion);

    this.nomencladorsSharedCollection.update(nomencladors =>
      this.nomencladorService.addNomencladorToCollectionIfMissing<INomenclador>(nomencladors, prestacion.nomenclador),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.nomencladorService
      .query()
      .pipe(map((res: HttpResponse<INomenclador[]>) => res.body ?? []))
      .pipe(
        map((nomencladors: INomenclador[]) =>
          this.nomencladorService.addNomencladorToCollectionIfMissing<INomenclador>(nomencladors, this.prestacion?.nomenclador),
        ),
      )
      .subscribe((nomencladors: INomenclador[]) => this.nomencladorsSharedCollection.set(nomencladors));
  }
}
