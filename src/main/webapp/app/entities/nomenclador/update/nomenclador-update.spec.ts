import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IMutual } from 'app/entities/mutual/mutual.model';
import { MutualService } from 'app/entities/mutual/service/mutual.service';
import { INomenclador } from '../nomenclador.model';
import { NomencladorService } from '../service/nomenclador.service';

import { NomencladorFormService } from './nomenclador-form.service';
import { NomencladorUpdate } from './nomenclador-update';

describe('Nomenclador Management Update Component', () => {
  let comp: NomencladorUpdate;
  let fixture: ComponentFixture<NomencladorUpdate>;
  let activatedRoute: ActivatedRoute;
  let nomencladorFormService: NomencladorFormService;
  let nomencladorService: NomencladorService;
  let mutualService: MutualService;

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

    fixture = TestBed.createComponent(NomencladorUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    nomencladorFormService = TestBed.inject(NomencladorFormService);
    nomencladorService = TestBed.inject(NomencladorService);
    mutualService = TestBed.inject(MutualService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Mutual query and add missing value', () => {
      const nomenclador: INomenclador = { id: 17440 };
      const mutual: IMutual = { id: 4576 };
      nomenclador.mutual = mutual;

      const mutualCollection: IMutual[] = [{ id: 4576 }];
      vi.spyOn(mutualService, 'query').mockReturnValue(of(new HttpResponse({ body: mutualCollection })));
      const additionalMutuals = [mutual];
      const expectedCollection: IMutual[] = [...additionalMutuals, ...mutualCollection];
      vi.spyOn(mutualService, 'addMutualToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ nomenclador });
      comp.ngOnInit();

      expect(mutualService.query).toHaveBeenCalled();
      expect(mutualService.addMutualToCollectionIfMissing).toHaveBeenCalledWith(
        mutualCollection,
        ...additionalMutuals.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.mutualsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const nomenclador: INomenclador = { id: 17440 };
      const mutual: IMutual = { id: 4576 };
      nomenclador.mutual = mutual;

      activatedRoute.data = of({ nomenclador });
      comp.ngOnInit();

      expect(comp.mutualsSharedCollection()).toContainEqual(mutual);
      expect(comp.nomenclador).toEqual(nomenclador);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<INomenclador>();
      const nomenclador = { id: 21757 };
      vi.spyOn(nomencladorFormService, 'getNomenclador').mockReturnValue(nomenclador);
      vi.spyOn(nomencladorService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nomenclador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(nomenclador);
      saveSubject.complete();

      // THEN
      expect(nomencladorFormService.getNomenclador).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(nomencladorService.update).toHaveBeenCalledWith(expect.objectContaining(nomenclador));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<INomenclador>();
      const nomenclador = { id: 21757 };
      vi.spyOn(nomencladorFormService, 'getNomenclador').mockReturnValue({ id: null });
      vi.spyOn(nomencladorService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nomenclador: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(nomenclador);
      saveSubject.complete();

      // THEN
      expect(nomencladorFormService.getNomenclador).toHaveBeenCalled();
      expect(nomencladorService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<INomenclador>();
      const nomenclador = { id: 21757 };
      vi.spyOn(nomencladorService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ nomenclador });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(nomencladorService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMutual', () => {
      it('should forward to mutualService', () => {
        const entity = { id: 4576 };
        const entity2 = { id: 14602 };
        vi.spyOn(mutualService, 'compareMutual');
        comp.compareMutual(entity, entity2);
        expect(mutualService.compareMutual).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
