import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { EstadoPaquete } from 'app/entities/enumerations/estado-paquete.model';
import { TipoIva } from 'app/entities/enumerations/tipo-iva.model';
import { IPlanMutual } from 'app/entities/plan-mutual/plan-mutual.model';
import { PlanMutualService } from 'app/entities/plan-mutual/service/plan-mutual.service';
import { AlertError } from 'app/shared/alert';
import { IPaquete } from '../paquete.model';
import { PaqueteService } from '../service/paquete.service';

import { PaqueteFormGroup, PaqueteFormService } from './paquete-form.service';

@Component({
  selector: 'jhi-paquete-update',
  templateUrl: './paquete-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class PaqueteUpdate implements OnInit {
  readonly isSaving = signal(false);
  paquete: IPaquete | null = null;
  estadoPaqueteValues = Object.keys(EstadoPaquete);
  tipoIvaValues = Object.keys(TipoIva);

  planMutualsSharedCollection = signal<IPlanMutual[]>([]);

  protected paqueteService = inject(PaqueteService);
  protected paqueteFormService = inject(PaqueteFormService);
  protected planMutualService = inject(PlanMutualService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaqueteFormGroup = this.paqueteFormService.createPaqueteFormGroup();

  comparePlanMutual = (o1: IPlanMutual | null, o2: IPlanMutual | null): boolean => this.planMutualService.comparePlanMutual(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paquete }) => {
      this.paquete = paquete;
      if (paquete) {
        this.updateForm(paquete);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const paquete = this.paqueteFormService.getPaquete(this.editForm);
    if (paquete.id === null) {
      this.subscribeToSaveResponse(this.paqueteService.create(paquete));
    } else {
      this.subscribeToSaveResponse(this.paqueteService.update(paquete));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPaquete | null>): void {
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

  protected updateForm(paquete: IPaquete): void {
    this.paquete = paquete;
    this.paqueteFormService.resetForm(this.editForm, paquete);

    this.planMutualsSharedCollection.update(planMutuals =>
      this.planMutualService.addPlanMutualToCollectionIfMissing<IPlanMutual>(planMutuals, paquete.plan),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.planMutualService
      .query()
      .pipe(map((res: HttpResponse<IPlanMutual[]>) => res.body ?? []))
      .pipe(
        map((planMutuals: IPlanMutual[]) =>
          this.planMutualService.addPlanMutualToCollectionIfMissing<IPlanMutual>(planMutuals, this.paquete?.plan),
        ),
      )
      .subscribe((planMutuals: IPlanMutual[]) => this.planMutualsSharedCollection.set(planMutuals));
  }
}
