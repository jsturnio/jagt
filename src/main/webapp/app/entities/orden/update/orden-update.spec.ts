import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IBioquimico } from 'app/entities/bioquimico/bioquimico.model';
import { BioquimicoService } from 'app/entities/bioquimico/service/bioquimico.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { IPaquete } from 'app/entities/paquete/paquete.model';
import { PaqueteService } from 'app/entities/paquete/service/paquete.service';
import { IOrden } from '../orden.model';
import { OrdenService } from '../service/orden.service';

import { OrdenFormService } from './orden-form.service';
import { OrdenUpdate } from './orden-update';

describe('Orden Management Update Component', () => {
  let comp: OrdenUpdate;
  let fixture: ComponentFixture<OrdenUpdate>;
  let activatedRoute: ActivatedRoute;
  let ordenFormService: OrdenFormService;
  let ordenService: OrdenService;
  let paqueteService: PaqueteService;
  let empleadoService: EmpleadoService;
  let bioquimicoService: BioquimicoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(OrdenUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ordenFormService = TestBed.inject(OrdenFormService);
    ordenService = TestBed.inject(OrdenService);
    paqueteService = TestBed.inject(PaqueteService);
    empleadoService = TestBed.inject(EmpleadoService);
    bioquimicoService = TestBed.inject(BioquimicoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Paquete query and add missing value', () => {
      const orden: IOrden = { id: 29983 };
      const paquete: IPaquete = { id: 23723 };
      orden.paquete = paquete;

      const paqueteCollection: IPaquete[] = [{ id: 23723 }];
      vi.spyOn(paqueteService, 'query').mockReturnValue(of(new HttpResponse({ body: paqueteCollection })));
      const additionalPaquetes = [paquete];
      const expectedCollection: IPaquete[] = [...additionalPaquetes, ...paqueteCollection];
      vi.spyOn(paqueteService, 'addPaqueteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      expect(paqueteService.query).toHaveBeenCalled();
      expect(paqueteService.addPaqueteToCollectionIfMissing).toHaveBeenCalledWith(
        paqueteCollection,
        ...additionalPaquetes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.paquetesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Empleado query and add missing value', () => {
      const orden: IOrden = { id: 29983 };
      const usuario: IEmpleado = { id: 11214 };
      orden.usuario = usuario;

      const empleadoCollection: IEmpleado[] = [{ id: 11214 }];
      vi.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [usuario];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      vi.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(
        empleadoCollection,
        ...additionalEmpleados.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.empleadosSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Bioquimico query and add missing value', () => {
      const orden: IOrden = { id: 29983 };
      const bioquimico: IBioquimico = { id: 24259 };
      orden.bioquimico = bioquimico;

      const bioquimicoCollection: IBioquimico[] = [{ id: 24259 }];
      vi.spyOn(bioquimicoService, 'query').mockReturnValue(of(new HttpResponse({ body: bioquimicoCollection })));
      const additionalBioquimicos = [bioquimico];
      const expectedCollection: IBioquimico[] = [...additionalBioquimicos, ...bioquimicoCollection];
      vi.spyOn(bioquimicoService, 'addBioquimicoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      expect(bioquimicoService.query).toHaveBeenCalled();
      expect(bioquimicoService.addBioquimicoToCollectionIfMissing).toHaveBeenCalledWith(
        bioquimicoCollection,
        ...additionalBioquimicos.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.bioquimicosSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const orden: IOrden = { id: 29983 };
      const paquete: IPaquete = { id: 23723 };
      orden.paquete = paquete;
      const usuario: IEmpleado = { id: 11214 };
      orden.usuario = usuario;
      const bioquimico: IBioquimico = { id: 24259 };
      orden.bioquimico = bioquimico;

      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      expect(comp.paquetesSharedCollection()).toContainEqual(paquete);
      expect(comp.empleadosSharedCollection()).toContainEqual(usuario);
      expect(comp.bioquimicosSharedCollection()).toContainEqual(bioquimico);
      expect(comp.orden).toEqual(orden);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrden>();
      const orden = { id: 10408 };
      vi.spyOn(ordenFormService, 'getOrden').mockReturnValue(orden);
      vi.spyOn(ordenService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orden);
      saveSubject.complete();

      // THEN
      expect(ordenFormService.getOrden).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ordenService.update).toHaveBeenCalledWith(expect.objectContaining(orden));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IOrden>();
      const orden = { id: 10408 };
      vi.spyOn(ordenFormService, 'getOrden').mockReturnValue({ id: null });
      vi.spyOn(ordenService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orden: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(orden);
      saveSubject.complete();

      // THEN
      expect(ordenFormService.getOrden).toHaveBeenCalled();
      expect(ordenService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IOrden>();
      const orden = { id: 10408 };
      vi.spyOn(ordenService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orden });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ordenService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePaquete', () => {
      it('should forward to paqueteService', () => {
        const entity = { id: 23723 };
        const entity2 = { id: 5295 };
        vi.spyOn(paqueteService, 'comparePaquete');
        comp.comparePaquete(entity, entity2);
        expect(paqueteService.comparePaquete).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEmpleado', () => {
      it('should forward to empleadoService', () => {
        const entity = { id: 11214 };
        const entity2 = { id: 25035 };
        vi.spyOn(empleadoService, 'compareEmpleado');
        comp.compareEmpleado(entity, entity2);
        expect(empleadoService.compareEmpleado).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBioquimico', () => {
      it('should forward to bioquimicoService', () => {
        const entity = { id: 24259 };
        const entity2 = { id: 1371 };
        vi.spyOn(bioquimicoService, 'compareBioquimico');
        comp.compareBioquimico(entity, entity2);
        expect(bioquimicoService.compareBioquimico).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
