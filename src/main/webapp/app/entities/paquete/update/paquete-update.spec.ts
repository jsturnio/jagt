import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IPlanMutual } from 'app/entities/plan-mutual/plan-mutual.model';
import { PlanMutualService } from 'app/entities/plan-mutual/service/plan-mutual.service';
import { IPaquete } from '../paquete.model';
import { PaqueteService } from '../service/paquete.service';

import { PaqueteFormService } from './paquete-form.service';
import { PaqueteUpdate } from './paquete-update';

describe('Paquete Management Update Component', () => {
  let comp: PaqueteUpdate;
  let fixture: ComponentFixture<PaqueteUpdate>;
  let activatedRoute: ActivatedRoute;
  let paqueteFormService: PaqueteFormService;
  let paqueteService: PaqueteService;
  let planMutualService: PlanMutualService;

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

    fixture = TestBed.createComponent(PaqueteUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paqueteFormService = TestBed.inject(PaqueteFormService);
    paqueteService = TestBed.inject(PaqueteService);
    planMutualService = TestBed.inject(PlanMutualService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PlanMutual query and add missing value', () => {
      const paquete: IPaquete = { id: 5295 };
      const plan: IPlanMutual = { id: 5137 };
      paquete.plan = plan;

      const planMutualCollection: IPlanMutual[] = [{ id: 5137 }];
      vi.spyOn(planMutualService, 'query').mockReturnValue(of(new HttpResponse({ body: planMutualCollection })));
      const additionalPlanMutuals = [plan];
      const expectedCollection: IPlanMutual[] = [...additionalPlanMutuals, ...planMutualCollection];
      vi.spyOn(planMutualService, 'addPlanMutualToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ paquete });
      comp.ngOnInit();

      expect(planMutualService.query).toHaveBeenCalled();
      expect(planMutualService.addPlanMutualToCollectionIfMissing).toHaveBeenCalledWith(
        planMutualCollection,
        ...additionalPlanMutuals.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.planMutualsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const paquete: IPaquete = { id: 5295 };
      const plan: IPlanMutual = { id: 5137 };
      paquete.plan = plan;

      activatedRoute.data = of({ paquete });
      comp.ngOnInit();

      expect(comp.planMutualsSharedCollection()).toContainEqual(plan);
      expect(comp.paquete).toEqual(paquete);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaquete>();
      const paquete = { id: 23723 };
      vi.spyOn(paqueteFormService, 'getPaquete').mockReturnValue(paquete);
      vi.spyOn(paqueteService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paquete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paquete);
      saveSubject.complete();

      // THEN
      expect(paqueteFormService.getPaquete).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paqueteService.update).toHaveBeenCalledWith(expect.objectContaining(paquete));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPaquete>();
      const paquete = { id: 23723 };
      vi.spyOn(paqueteFormService, 'getPaquete').mockReturnValue({ id: null });
      vi.spyOn(paqueteService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paquete: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(paquete);
      saveSubject.complete();

      // THEN
      expect(paqueteFormService.getPaquete).toHaveBeenCalled();
      expect(paqueteService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPaquete>();
      const paquete = { id: 23723 };
      vi.spyOn(paqueteService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ paquete });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paqueteService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePlanMutual', () => {
      it('should forward to planMutualService', () => {
        const entity = { id: 5137 };
        const entity2 = { id: 28218 };
        vi.spyOn(planMutualService, 'comparePlanMutual');
        comp.comparePlanMutual(entity, entity2);
        expect(planMutualService.comparePlanMutual).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
