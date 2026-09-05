import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IMutual } from 'app/entities/mutual/mutual.model';
import { MutualService } from 'app/entities/mutual/service/mutual.service';
import { AlertError } from 'app/shared/alert';
import { IPlanMutual } from '../plan-mutual.model';
import { PlanMutualService } from '../service/plan-mutual.service';

import { PlanMutualFormGroup, PlanMutualFormService } from './plan-mutual-form.service';

@Component({
  selector: 'jhi-plan-mutual-update',
  templateUrl: './plan-mutual-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class PlanMutualUpdate implements OnInit {
  readonly isSaving = signal(false);
  planMutual: IPlanMutual | null = null;

  mutualsSharedCollection = signal<IMutual[]>([]);

  protected planMutualService = inject(PlanMutualService);
  protected planMutualFormService = inject(PlanMutualFormService);
  protected mutualService = inject(MutualService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlanMutualFormGroup = this.planMutualFormService.createPlanMutualFormGroup();

  compareMutual = (o1: IMutual | null, o2: IMutual | null): boolean => this.mutualService.compareMutual(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planMutual }) => {
      this.planMutual = planMutual;
      if (planMutual) {
        this.updateForm(planMutual);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const planMutual = this.planMutualFormService.getPlanMutual(this.editForm);
    if (planMutual.id === null) {
      this.subscribeToSaveResponse(this.planMutualService.create(planMutual));
    } else {
      this.subscribeToSaveResponse(this.planMutualService.update(planMutual));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPlanMutual | null>): void {
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

  protected updateForm(planMutual: IPlanMutual): void {
    this.planMutual = planMutual;
    this.planMutualFormService.resetForm(this.editForm, planMutual);

    this.mutualsSharedCollection.update(mutuals => this.mutualService.addMutualToCollectionIfMissing<IMutual>(mutuals, planMutual.mutual));
  }

  protected loadRelationshipsOptions(): void {
    this.mutualService
      .query()
      .pipe(map((res: HttpResponse<IMutual[]>) => res.body ?? []))
      .pipe(map((mutuals: IMutual[]) => this.mutualService.addMutualToCollectionIfMissing<IMutual>(mutuals, this.planMutual?.mutual)))
      .subscribe((mutuals: IMutual[]) => this.mutualsSharedCollection.set(mutuals));
  }
}
