import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../paquete.test-samples';

import { PaqueteFormService } from './paquete-form.service';

describe('Paquete Form Service', () => {
  let service: PaqueteFormService;

  beforeEach(() => {
    service = TestBed.inject(PaqueteFormService);
  });

  describe('Service methods', () => {
    describe('createPaqueteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPaqueteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            paqDescrip: expect.any(Object),
            nombre: expect.any(Object),
            periodo: expect.any(Object),
            estado: expect.any(Object),
            descripcion: expect.any(Object),
            tipoIva: expect.any(Object),
            plan: expect.any(Object),
          }),
        );
      });

      it('passing IPaquete should create a new form with FormGroup', () => {
        const formGroup = service.createPaqueteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            paqDescrip: expect.any(Object),
            nombre: expect.any(Object),
            periodo: expect.any(Object),
            estado: expect.any(Object),
            descripcion: expect.any(Object),
            tipoIva: expect.any(Object),
            plan: expect.any(Object),
          }),
        );
      });
    });

    describe('getPaquete', () => {
      it('should return NewPaquete for default Paquete initial value', () => {
        const formGroup = service.createPaqueteFormGroup(sampleWithNewData);

        const paquete = service.getPaquete(formGroup);

        expect(paquete).toMatchObject(sampleWithNewData);
      });

      it('should return NewPaquete for empty Paquete initial value', () => {
        const formGroup = service.createPaqueteFormGroup();

        const paquete = service.getPaquete(formGroup);

        expect(paquete).toMatchObject({});
      });

      it('should return IPaquete', () => {
        const formGroup = service.createPaqueteFormGroup(sampleWithRequiredData);

        const paquete = service.getPaquete(formGroup);

        expect(paquete).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPaquete should not enable id FormControl', () => {
        const formGroup = service.createPaqueteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPaquete should disable id FormControl', () => {
        const formGroup = service.createPaqueteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
