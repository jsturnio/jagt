import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bioquimico.test-samples';

import { BioquimicoFormService } from './bioquimico-form.service';

describe('Bioquimico Form Service', () => {
  let service: BioquimicoFormService;

  beforeEach(() => {
    service = TestBed.inject(BioquimicoFormService);
  });

  describe('Service methods', () => {
    describe('createBioquimicoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBioquimicoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matricula: expect.any(Object),
            cuit: expect.any(Object),
            tipoIva: expect.any(Object),
            domicilioProfesional: expect.any(Object),
            estado: expect.any(Object),
            email: expect.any(Object),
            fechaIngreso: expect.any(Object),
            telefono: expect.any(Object),
            nombreEnDosep: expect.any(Object),
            nroPrestadorOsde: expect.any(Object),
            ingBrutos: expect.any(Object),
            nroJubilacion: expect.any(Object),
            nroLaboratorio: expect.any(Object),
            nombreCompleto: expect.any(Object),
            genero: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IBioquimico should create a new form with FormGroup', () => {
        const formGroup = service.createBioquimicoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            matricula: expect.any(Object),
            cuit: expect.any(Object),
            tipoIva: expect.any(Object),
            domicilioProfesional: expect.any(Object),
            estado: expect.any(Object),
            email: expect.any(Object),
            fechaIngreso: expect.any(Object),
            telefono: expect.any(Object),
            nombreEnDosep: expect.any(Object),
            nroPrestadorOsde: expect.any(Object),
            ingBrutos: expect.any(Object),
            nroJubilacion: expect.any(Object),
            nroLaboratorio: expect.any(Object),
            nombreCompleto: expect.any(Object),
            genero: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getBioquimico', () => {
      it('should return NewBioquimico for default Bioquimico initial value', () => {
        const formGroup = service.createBioquimicoFormGroup(sampleWithNewData);

        const bioquimico = service.getBioquimico(formGroup);

        expect(bioquimico).toMatchObject(sampleWithNewData);
      });

      it('should return NewBioquimico for empty Bioquimico initial value', () => {
        const formGroup = service.createBioquimicoFormGroup();

        const bioquimico = service.getBioquimico(formGroup);

        expect(bioquimico).toMatchObject({});
      });

      it('should return IBioquimico', () => {
        const formGroup = service.createBioquimicoFormGroup(sampleWithRequiredData);

        const bioquimico = service.getBioquimico(formGroup);

        expect(bioquimico).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBioquimico should not enable id FormControl', () => {
        const formGroup = service.createBioquimicoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBioquimico should disable id FormControl', () => {
        const formGroup = service.createBioquimicoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
