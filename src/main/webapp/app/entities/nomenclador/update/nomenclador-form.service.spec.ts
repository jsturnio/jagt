import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../nomenclador.test-samples';

import { NomencladorFormService } from './nomenclador-form.service';

describe('Nomenclador Form Service', () => {
  let service: NomencladorFormService;

  beforeEach(() => {
    service = TestBed.inject(NomencladorFormService);
  });

  describe('Service methods', () => {
    describe('createNomencladorFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createNomencladorFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            tipo: expect.any(Object),
            mutual: expect.any(Object),
          }),
        );
      });

      it('passing INomenclador should create a new form with FormGroup', () => {
        const formGroup = service.createNomencladorFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            tipo: expect.any(Object),
            mutual: expect.any(Object),
          }),
        );
      });
    });

    describe('getNomenclador', () => {
      it('should return NewNomenclador for default Nomenclador initial value', () => {
        const formGroup = service.createNomencladorFormGroup(sampleWithNewData);

        const nomenclador = service.getNomenclador(formGroup);

        expect(nomenclador).toMatchObject(sampleWithNewData);
      });

      it('should return NewNomenclador for empty Nomenclador initial value', () => {
        const formGroup = service.createNomencladorFormGroup();

        const nomenclador = service.getNomenclador(formGroup);

        expect(nomenclador).toMatchObject({});
      });

      it('should return INomenclador', () => {
        const formGroup = service.createNomencladorFormGroup(sampleWithRequiredData);

        const nomenclador = service.getNomenclador(formGroup);

        expect(nomenclador).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing INomenclador should not enable id FormControl', () => {
        const formGroup = service.createNomencladorFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewNomenclador should disable id FormControl', () => {
        const formGroup = service.createNomencladorFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
