import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../plan-mutual.test-samples';

import { PlanMutualFormService } from './plan-mutual-form.service';

describe('PlanMutual Form Service', () => {
  let service: PlanMutualFormService;

  beforeEach(() => {
    service = TestBed.inject(PlanMutualFormService);
  });

  describe('Service methods', () => {
    describe('createPlanMutualFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlanMutualFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            categoria: expect.any(Object),
            etiquetaReporte: expect.any(Object),
            prestacionesImportadas: expect.any(Object),
            mutual: expect.any(Object),
          }),
        );
      });

      it('passing IPlanMutual should create a new form with FormGroup', () => {
        const formGroup = service.createPlanMutualFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            categoria: expect.any(Object),
            etiquetaReporte: expect.any(Object),
            prestacionesImportadas: expect.any(Object),
            mutual: expect.any(Object),
          }),
        );
      });
    });

    describe('getPlanMutual', () => {
      it('should return NewPlanMutual for default PlanMutual initial value', () => {
        const formGroup = service.createPlanMutualFormGroup(sampleWithNewData);

        const planMutual = service.getPlanMutual(formGroup);

        expect(planMutual).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlanMutual for empty PlanMutual initial value', () => {
        const formGroup = service.createPlanMutualFormGroup();

        const planMutual = service.getPlanMutual(formGroup);

        expect(planMutual).toMatchObject({});
      });

      it('should return IPlanMutual', () => {
        const formGroup = service.createPlanMutualFormGroup(sampleWithRequiredData);

        const planMutual = service.getPlanMutual(formGroup);

        expect(planMutual).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlanMutual should not enable id FormControl', () => {
        const formGroup = service.createPlanMutualFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlanMutual should disable id FormControl', () => {
        const formGroup = service.createPlanMutualFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
