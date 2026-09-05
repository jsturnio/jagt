import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IOrden } from 'app/entities/orden/orden.model';
import { OrdenService } from 'app/entities/orden/service/orden.service';
import { IPrestacion } from 'app/entities/prestacion/prestacion.model';
import { PrestacionService } from 'app/entities/prestacion/service/prestacion.service';
import { IPractica } from '../practica.model';
import { PracticaService } from '../service/practica.service';

import { PracticaFormService } from './practica-form.service';
import { PracticaUpdate } from './practica-update';

describe('Practica Management Update Component', () => {
  let comp: PracticaUpdate;
  let fixture: ComponentFixture<PracticaUpdate>;
  let activatedRoute: ActivatedRoute;
  let practicaFormService: PracticaFormService;
  let practicaService: PracticaService;
  let prestacionService: PrestacionService;
  let ordenService: OrdenService;

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

    fixture = TestBed.createComponent(PracticaUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    practicaFormService = TestBed.inject(PracticaFormService);
    practicaService = TestBed.inject(PracticaService);
    prestacionService = TestBed.inject(PrestacionService);
    ordenService = TestBed.inject(OrdenService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Prestacion query and add missing value', () => {
      const practica: IPractica = { id: 14030 };
      const prestacion: IPrestacion = { id: 17259 };
      practica.prestacion = prestacion;

      const prestacionCollection: IPrestacion[] = [{ id: 17259 }];
      vi.spyOn(prestacionService, 'query').mockReturnValue(of(new HttpResponse({ body: prestacionCollection })));
      const additionalPrestacions = [prestacion];
      const expectedCollection: IPrestacion[] = [...additionalPrestacions, ...prestacionCollection];
      vi.spyOn(prestacionService, 'addPrestacionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ practica });
      comp.ngOnInit();

      expect(prestacionService.query).toHaveBeenCalled();
      expect(prestacionService.addPrestacionToCollectionIfMissing).toHaveBeenCalledWith(
        prestacionCollection,
        ...additionalPrestacions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.prestacionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Orden query and add missing value', () => {
      const practica: IPractica = { id: 14030 };
      const orden: IOrden = { id: 10408 };
      practica.orden = orden;

      const ordenCollection: IOrden[] = [{ id: 10408 }];
      vi.spyOn(ordenService, 'query').mockReturnValue(of(new HttpResponse({ body: ordenCollection })));
      const additionalOrdens = [orden];
      const expectedCollection: IOrden[] = [...additionalOrdens, ...ordenCollection];
      vi.spyOn(ordenService, 'addOrdenToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ practica });
      comp.ngOnInit();

      expect(ordenService.query).toHaveBeenCalled();
      expect(ordenService.addOrdenToCollectionIfMissing).toHaveBeenCalledWith(
        ordenCollection,
        ...additionalOrdens.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.ordensSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const practica: IPractica = { id: 14030 };
      const prestacion: IPrestacion = { id: 17259 };
      practica.prestacion = prestacion;
      const orden: IOrden = { id: 10408 };
      practica.orden = orden;

      activatedRoute.data = of({ practica });
      comp.ngOnInit();

      expect(comp.prestacionsSharedCollection()).toContainEqual(prestacion);
      expect(comp.ordensSharedCollection()).toContainEqual(orden);
      expect(comp.practica).toEqual(practica);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPractica>();
      const practica = { id: 7449 };
      vi.spyOn(practicaFormService, 'getPractica').mockReturnValue(practica);
      vi.spyOn(practicaService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ practica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(practica);
      saveSubject.complete();

      // THEN
      expect(practicaFormService.getPractica).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(practicaService.update).toHaveBeenCalledWith(expect.objectContaining(practica));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPractica>();
      const practica = { id: 7449 };
      vi.spyOn(practicaFormService, 'getPractica').mockReturnValue({ id: null });
      vi.spyOn(practicaService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ practica: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(practica);
      saveSubject.complete();

      // THEN
      expect(practicaFormService.getPractica).toHaveBeenCalled();
      expect(practicaService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPractica>();
      const practica = { id: 7449 };
      vi.spyOn(practicaService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ practica });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(practicaService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePrestacion', () => {
      it('should forward to prestacionService', () => {
        const entity = { id: 17259 };
        const entity2 = { id: 23229 };
        vi.spyOn(prestacionService, 'comparePrestacion');
        comp.comparePrestacion(entity, entity2);
        expect(prestacionService.comparePrestacion).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareOrden', () => {
      it('should forward to ordenService', () => {
        const entity = { id: 10408 };
        const entity2 = { id: 29983 };
        vi.spyOn(ordenService, 'compareOrden');
        comp.compareOrden(entity, entity2);
        expect(ordenService.compareOrden).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
