import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../practica.test-samples';

import { PracticaFormService } from './practica-form.service';

describe('Practica Form Service', () => {
  let service: PracticaFormService;

  beforeEach(() => {
    service = TestBed.inject(PracticaFormService);
  });

  describe('Service methods', () => {
    describe('createPracticaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPracticaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidad: expect.any(Object),
            pvalor: expect.any(Object),
            prestacion: expect.any(Object),
            orden: expect.any(Object),
          }),
        );
      });

      it('passing IPractica should create a new form with FormGroup', () => {
        const formGroup = service.createPracticaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cantidad: expect.any(Object),
            pvalor: expect.any(Object),
            prestacion: expect.any(Object),
            orden: expect.any(Object),
          }),
        );
      });
    });

    describe('getPractica', () => {
      it('should return NewPractica for default Practica initial value', () => {
        const formGroup = service.createPracticaFormGroup(sampleWithNewData);

        const practica = service.getPractica(formGroup);

        expect(practica).toMatchObject(sampleWithNewData);
      });

      it('should return NewPractica for empty Practica initial value', () => {
        const formGroup = service.createPracticaFormGroup();

        const practica = service.getPractica(formGroup);

        expect(practica).toMatchObject({});
      });

      it('should return IPractica', () => {
        const formGroup = service.createPracticaFormGroup(sampleWithRequiredData);

        const practica = service.getPractica(formGroup);

        expect(practica).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPractica should not enable id FormControl', () => {
        const formGroup = service.createPracticaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPractica should disable id FormControl', () => {
        const formGroup = service.createPracticaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
