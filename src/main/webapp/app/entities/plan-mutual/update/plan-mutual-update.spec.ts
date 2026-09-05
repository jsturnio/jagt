import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IMutual } from 'app/entities/mutual/mutual.model';
import { MutualService } from 'app/entities/mutual/service/mutual.service';
import { IPlanMutual } from '../plan-mutual.model';
import { PlanMutualService } from '../service/plan-mutual.service';

import { PlanMutualFormService } from './plan-mutual-form.service';
import { PlanMutualUpdate } from './plan-mutual-update';

describe('PlanMutual Management Update Component', () => {
  let comp: PlanMutualUpdate;
  let fixture: ComponentFixture<PlanMutualUpdate>;
  let activatedRoute: ActivatedRoute;
  let planMutualFormService: PlanMutualFormService;
  let planMutualService: PlanMutualService;
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

    fixture = TestBed.createComponent(PlanMutualUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    planMutualFormService = TestBed.inject(PlanMutualFormService);
    planMutualService = TestBed.inject(PlanMutualService);
    mutualService = TestBed.inject(MutualService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Mutual query and add missing value', () => {
      const planMutual: IPlanMutual = { id: 28218 };
      const mutual: IMutual = { id: 4576 };
      planMutual.mutual = mutual;

      const mutualCollection: IMutual[] = [{ id: 4576 }];
      vi.spyOn(mutualService, 'query').mockReturnValue(of(new HttpResponse({ body: mutualCollection })));
      const additionalMutuals = [mutual];
      const expectedCollection: IMutual[] = [...additionalMutuals, ...mutualCollection];
      vi.spyOn(mutualService, 'addMutualToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ planMutual });
      comp.ngOnInit();

      expect(mutualService.query).toHaveBeenCalled();
      expect(mutualService.addMutualToCollectionIfMissing).toHaveBeenCalledWith(
        mutualCollection,
        ...additionalMutuals.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.mutualsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const planMutual: IPlanMutual = { id: 28218 };
      const mutual: IMutual = { id: 4576 };
      planMutual.mutual = mutual;

      activatedRoute.data = of({ planMutual });
      comp.ngOnInit();

      expect(comp.mutualsSharedCollection()).toContainEqual(mutual);
      expect(comp.planMutual).toEqual(planMutual);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPlanMutual>();
      const planMutual = { id: 5137 };
      vi.spyOn(planMutualFormService, 'getPlanMutual').mockReturnValue(planMutual);
      vi.spyOn(planMutualService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planMutual });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(planMutual);
      saveSubject.complete();

      // THEN
      expect(planMutualFormService.getPlanMutual).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(planMutualService.update).toHaveBeenCalledWith(expect.objectContaining(planMutual));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPlanMutual>();
      const planMutual = { id: 5137 };
      vi.spyOn(planMutualFormService, 'getPlanMutual').mockReturnValue({ id: null });
      vi.spyOn(planMutualService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planMutual: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(planMutual);
      saveSubject.complete();

      // THEN
      expect(planMutualFormService.getPlanMutual).toHaveBeenCalled();
      expect(planMutualService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPlanMutual>();
      const planMutual = { id: 5137 };
      vi.spyOn(planMutualService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planMutual });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(planMutualService.update).toHaveBeenCalled();
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
