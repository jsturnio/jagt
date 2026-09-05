import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../mutual.test-samples';

import { MutualFormService } from './mutual-form.service';

describe('Mutual Form Service', () => {
  let service: MutualFormService;

  beforeEach(() => {
    service = TestBed.inject(MutualFormService);
  });

  describe('Service methods', () => {
    describe('createMutualFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMutualFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            cuit: expect.any(Object),
            tipoIva: expect.any(Object),
            domicilio: expect.any(Object),
            telefono: expect.any(Object),
            email: expect.any(Object),
            habilitada: expect.any(Object),
          }),
        );
      });

      it('passing IMutual should create a new form with FormGroup', () => {
        const formGroup = service.createMutualFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            cuit: expect.any(Object),
            tipoIva: expect.any(Object),
            domicilio: expect.any(Object),
            telefono: expect.any(Object),
            email: expect.any(Object),
            habilitada: expect.any(Object),
          }),
        );
      });
    });

    describe('getMutual', () => {
      it('should return NewMutual for default Mutual initial value', () => {
        const formGroup = service.createMutualFormGroup(sampleWithNewData);

        const mutual = service.getMutual(formGroup);

        expect(mutual).toMatchObject(sampleWithNewData);
      });

      it('should return NewMutual for empty Mutual initial value', () => {
        const formGroup = service.createMutualFormGroup();

        const mutual = service.getMutual(formGroup);

        expect(mutual).toMatchObject({});
      });

      it('should return IMutual', () => {
        const formGroup = service.createMutualFormGroup(sampleWithRequiredData);

        const mutual = service.getMutual(formGroup);

        expect(mutual).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMutual should not enable id FormControl', () => {
        const formGroup = service.createMutualFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMutual should disable id FormControl', () => {
        const formGroup = service.createMutualFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
