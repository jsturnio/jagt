import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { IMutual } from '../mutual.model';
import { MutualService } from '../service/mutual.service';

import { MutualFormService } from './mutual-form.service';
import { MutualUpdate } from './mutual-update';

describe('Mutual Management Update Component', () => {
  let comp: MutualUpdate;
  let fixture: ComponentFixture<MutualUpdate>;
  let activatedRoute: ActivatedRoute;
  let mutualFormService: MutualFormService;
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

    fixture = TestBed.createComponent(MutualUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mutualFormService = TestBed.inject(MutualFormService);
    mutualService = TestBed.inject(MutualService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const mutual: IMutual = { id: 14602 };

      activatedRoute.data = of({ mutual });
      comp.ngOnInit();

      expect(comp.mutual).toEqual(mutual);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMutual>();
      const mutual = { id: 4576 };
      vi.spyOn(mutualFormService, 'getMutual').mockReturnValue(mutual);
      vi.spyOn(mutualService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mutual });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(mutual);
      saveSubject.complete();

      // THEN
      expect(mutualFormService.getMutual).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mutualService.update).toHaveBeenCalledWith(expect.objectContaining(mutual));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMutual>();
      const mutual = { id: 4576 };
      vi.spyOn(mutualFormService, 'getMutual').mockReturnValue({ id: null });
      vi.spyOn(mutualService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mutual: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(mutual);
      saveSubject.complete();

      // THEN
      expect(mutualFormService.getMutual).toHaveBeenCalled();
      expect(mutualService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IMutual>();
      const mutual = { id: 4576 };
      vi.spyOn(mutualService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mutual });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mutualService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
