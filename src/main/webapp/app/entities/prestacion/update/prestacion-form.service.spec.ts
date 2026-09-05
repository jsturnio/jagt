import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../prestacion.test-samples';

import { PrestacionFormService } from './prestacion-form.service';

describe('Prestacion Form Service', () => {
  let service: PrestacionFormService;

  beforeEach(() => {
    service = TestBed.inject(PrestacionFormService);
  });

  describe('Service methods', () => {
    describe('createPrestacionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPrestacionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            codigoInos: expect.any(Object),
            valorUb: expect.any(Object),
            reqAutorizacion: expect.any(Object),
            nomenclador: expect.any(Object),
          }),
        );
      });

      it('passing IPrestacion should create a new form with FormGroup', () => {
        const formGroup = service.createPrestacionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            codigoInos: expect.any(Object),
            valorUb: expect.any(Object),
            reqAutorizacion: expect.any(Object),
            nomenclador: expect.any(Object),
          }),
        );
      });
    });

    describe('getPrestacion', () => {
      it('should return NewPrestacion for default Prestacion initial value', () => {
        const formGroup = service.createPrestacionFormGroup(sampleWithNewData);

        const prestacion = service.getPrestacion(formGroup);

        expect(prestacion).toMatchObject(sampleWithNewData);
      });

      it('should return NewPrestacion for empty Prestacion initial value', () => {
        const formGroup = service.createPrestacionFormGroup();

        const prestacion = service.getPrestacion(formGroup);

        expect(prestacion).toMatchObject({});
      });

      it('should return IPrestacion', () => {
        const formGroup = service.createPrestacionFormGroup(sampleWithRequiredData);

        const prestacion = service.getPrestacion(formGroup);

        expect(prestacion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPrestacion should not enable id FormControl', () => {
        const formGroup = service.createPrestacionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPrestacion should disable id FormControl', () => {
        const formGroup = service.createPrestacionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
