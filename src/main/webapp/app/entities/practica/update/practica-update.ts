import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IOrden } from 'app/entities/orden/orden.model';
import { OrdenService } from 'app/entities/orden/service/orden.service';
import { IPrestacion } from 'app/entities/prestacion/prestacion.model';
import { PrestacionService } from 'app/entities/prestacion/service/prestacion.service';
import { AlertError } from 'app/shared/alert';
import { IPractica } from '../practica.model';
import { PracticaService } from '../service/practica.service';

import { PracticaFormGroup, PracticaFormService } from './practica-form.service';

@Component({
  selector: 'jhi-practica-update',
  templateUrl: './practica-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PracticaUpdate implements OnInit {
  readonly isSaving = signal(false);
  practica: IPractica | null = null;

  prestacionsSharedCollection = signal<IPrestacion[]>([]);
  ordensSharedCollection = signal<IOrden[]>([]);

  protected practicaService = inject(PracticaService);
  protected practicaFormService = inject(PracticaFormService);
  protected prestacionService = inject(PrestacionService);
  protected ordenService = inject(OrdenService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PracticaFormGroup = this.practicaFormService.createPracticaFormGroup();

  comparePrestacion = (o1: IPrestacion | null, o2: IPrestacion | null): boolean => this.prestacionService.comparePrestacion(o1, o2);

  compareOrden = (o1: IOrden | null, o2: IOrden | null): boolean => this.ordenService.compareOrden(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ practica }) => {
      this.practica = practica;
      if (practica) {
        this.updateForm(practica);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const practica = this.practicaFormService.getPractica(this.editForm);
    if (practica.id === null) {
      this.subscribeToSaveResponse(this.practicaService.create(practica));
    } else {
      this.subscribeToSaveResponse(this.practicaService.update(practica));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPractica | null>): void {
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

  protected updateForm(practica: IPractica): void {
    this.practica = practica;
    this.practicaFormService.resetForm(this.editForm, practica);

    this.prestacionsSharedCollection.update(prestacions =>
      this.prestacionService.addPrestacionToCollectionIfMissing<IPrestacion>(prestacions, practica.prestacion),
    );
    this.ordensSharedCollection.update(ordens => this.ordenService.addOrdenToCollectionIfMissing<IOrden>(ordens, practica.orden));
  }

  protected loadRelationshipsOptions(): void {
    this.prestacionService
      .query()
      .pipe(map((res: HttpResponse<IPrestacion[]>) => res.body ?? []))
      .pipe(
        map((prestacions: IPrestacion[]) =>
          this.prestacionService.addPrestacionToCollectionIfMissing<IPrestacion>(prestacions, this.practica?.prestacion),
        ),
      )
      .subscribe((prestacions: IPrestacion[]) => this.prestacionsSharedCollection.set(prestacions));

    this.ordenService
      .query()
      .pipe(map((res: HttpResponse<IOrden[]>) => res.body ?? []))
      .pipe(map((ordens: IOrden[]) => this.ordenService.addOrdenToCollectionIfMissing<IOrden>(ordens, this.practica?.orden)))
      .subscribe((ordens: IOrden[]) => this.ordensSharedCollection.set(ordens));
  }
}
