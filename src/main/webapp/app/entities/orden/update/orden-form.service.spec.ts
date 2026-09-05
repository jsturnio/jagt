import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../orden.test-samples';

import { OrdenFormService } from './orden-form.service';

describe('Orden Form Service', () => {
  let service: OrdenFormService;

  beforeEach(() => {
    service = TestBed.inject(OrdenFormService);
  });

  describe('Service methods', () => {
    describe('createOrdenFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrdenFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            afiliado: expect.any(Object),
            protocolo: expect.any(Object),
            fechaOrden: expect.any(Object),
            fechaPrescripcion: expect.any(Object),
            totalOrden: expect.any(Object),
            sumaUb: expect.any(Object),
            fechaCreacion: expect.any(Object),
            paquete: expect.any(Object),
            usuario: expect.any(Object),
            bioquimico: expect.any(Object),
          }),
        );
      });

      it('passing IOrden should create a new form with FormGroup', () => {
        const formGroup = service.createOrdenFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            afiliado: expect.any(Object),
            protocolo: expect.any(Object),
            fechaOrden: expect.any(Object),
            fechaPrescripcion: expect.any(Object),
            totalOrden: expect.any(Object),
            sumaUb: expect.any(Object),
            fechaCreacion: expect.any(Object),
            paquete: expect.any(Object),
            usuario: expect.any(Object),
            bioquimico: expect.any(Object),
          }),
        );
      });
    });

    describe('getOrden', () => {
      it('should return NewOrden for default Orden initial value', () => {
        const formGroup = service.createOrdenFormGroup(sampleWithNewData);

        const orden = service.getOrden(formGroup);

        expect(orden).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrden for empty Orden initial value', () => {
        const formGroup = service.createOrdenFormGroup();

        const orden = service.getOrden(formGroup);

        expect(orden).toMatchObject({});
      });

      it('should return IOrden', () => {
        const formGroup = service.createOrdenFormGroup(sampleWithRequiredData);

        const orden = service.getOrden(formGroup);

        expect(orden).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrden should not enable id FormControl', () => {
        const formGroup = service.createOrdenFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrden should disable id FormControl', () => {
        const formGroup = service.createOrdenFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
