import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { INomenclador } from 'app/entities/nomenclador/nomenclador.model';
import { NomencladorService } from 'app/entities/nomenclador/service/nomenclador.service';
import { IPrestacion } from '../prestacion.model';
import { PrestacionService } from '../service/prestacion.service';

import { PrestacionFormService } from './prestacion-form.service';
import { PrestacionUpdate } from './prestacion-update';

describe('Prestacion Management Update Component', () => {
  let comp: PrestacionUpdate;
  let fixture: ComponentFixture<PrestacionUpdate>;
  let activatedRoute: ActivatedRoute;
  let prestacionFormService: PrestacionFormService;
  let prestacionService: PrestacionService;
  let nomencladorService: NomencladorService;

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

    fixture = TestBed.createComponent(PrestacionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    prestacionFormService = TestBed.inject(PrestacionFormService);
    prestacionService = TestBed.inject(PrestacionService);
    nomencladorService = TestBed.inject(NomencladorService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Nomenclador query and add missing value', () => {
      const prestacion: IPrestacion = { id: 23229 };
      const nomenclador: INomenclador = { id: 21757 };
      prestacion.nomenclador = nomenclador;

      const nomencladorCollection: INomenclador[] = [{ id: 21757 }];
      vi.spyOn(nomencladorService, 'query').mockReturnValue(of(new HttpResponse({ body: nomencladorCollection })));
      const additionalNomencladors = [nomenclador];
      const expectedCollection: INomenclador[] = [...additionalNomencladors, ...nomencladorCollection];
      vi.spyOn(nomencladorService, 'addNomencladorToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ prestacion });
      comp.ngOnInit();

      expect(nomencladorService.query).toHaveBeenCalled();
      expect(nomencladorService.addNomencladorToCollectionIfMissing).toHaveBeenCalledWith(
        nomencladorCollection,
        ...additionalNomencladors.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.nomencladorsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const prestacion: IPrestacion = { id: 23229 };
      const nomenclador: INomenclador = { id: 21757 };
      prestacion.nomenclador = nomenclador;

      activatedRoute.data = of({ prestacion });
      comp.ngOnInit();

      expect(comp.nomencladorsSharedCollection()).toContainEqual(nomenclador);
      expect(comp.prestacion).toEqual(prestacion);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPrestacion>();
      const prestacion = { id: 17259 };
      vi.spyOn(prestacionFormService, 'getPrestacion').mockReturnValue(prestacion);
      vi.spyOn(prestacionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(prestacion);
      saveSubject.complete();

      // THEN
      expect(prestacionFormService.getPrestacion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(prestacionService.update).toHaveBeenCalledWith(expect.objectContaining(prestacion));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPrestacion>();
      const prestacion = { id: 17259 };
      vi.spyOn(prestacionFormService, 'getPrestacion').mockReturnValue({ id: null });
      vi.spyOn(prestacionService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestacion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(prestacion);
      saveSubject.complete();

      // THEN
      expect(prestacionFormService.getPrestacion).toHaveBeenCalled();
      expect(prestacionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPrestacion>();
      const prestacion = { id: 17259 };
      vi.spyOn(prestacionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ prestacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(prestacionService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareNomenclador', () => {
      it('should forward to nomencladorService', () => {
        const entity = { id: 21757 };
        const entity2 = { id: 17440 };
        vi.spyOn(nomencladorService, 'compareNomenclador');
        comp.compareNomenclador(entity, entity2);
        expect(nomencladorService.compareNomenclador).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
