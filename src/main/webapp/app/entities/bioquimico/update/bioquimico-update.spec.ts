import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IBioquimico } from '../bioquimico.model';
import { BioquimicoService } from '../service/bioquimico.service';

import { BioquimicoFormService } from './bioquimico-form.service';
import { BioquimicoUpdate } from './bioquimico-update';

describe('Bioquimico Management Update Component', () => {
  let comp: BioquimicoUpdate;
  let fixture: ComponentFixture<BioquimicoUpdate>;
  let activatedRoute: ActivatedRoute;
  let bioquimicoFormService: BioquimicoFormService;
  let bioquimicoService: BioquimicoService;
  let userService: UserService;

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

    fixture = TestBed.createComponent(BioquimicoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bioquimicoFormService = TestBed.inject(BioquimicoFormService);
    bioquimicoService = TestBed.inject(BioquimicoService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const bioquimico: IBioquimico = { id: 1371 };
      const user: IUser = { id: 3944 };
      bioquimico.user = user;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bioquimico });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const bioquimico: IBioquimico = { id: 1371 };
      const user: IUser = { id: 3944 };
      bioquimico.user = user;

      activatedRoute.data = of({ bioquimico });
      comp.ngOnInit();

      expect(comp.usersSharedCollection()).toContainEqual(user);
      expect(comp.bioquimico).toEqual(bioquimico);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IBioquimico>();
      const bioquimico = { id: 24259 };
      vi.spyOn(bioquimicoFormService, 'getBioquimico').mockReturnValue(bioquimico);
      vi.spyOn(bioquimicoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bioquimico });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(bioquimico);
      saveSubject.complete();

      // THEN
      expect(bioquimicoFormService.getBioquimico).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bioquimicoService.update).toHaveBeenCalledWith(expect.objectContaining(bioquimico));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IBioquimico>();
      const bioquimico = { id: 24259 };
      vi.spyOn(bioquimicoFormService, 'getBioquimico').mockReturnValue({ id: null });
      vi.spyOn(bioquimicoService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bioquimico: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(bioquimico);
      saveSubject.complete();

      // THEN
      expect(bioquimicoFormService.getBioquimico).toHaveBeenCalled();
      expect(bioquimicoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IBioquimico>();
      const bioquimico = { id: 24259 };
      vi.spyOn(bioquimicoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bioquimico });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bioquimicoService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vi.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
