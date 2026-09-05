import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { Subject, from, of } from 'rxjs';

import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';

import { EmpleadoFormService } from './empleado-form.service';
import { EmpleadoUpdate } from './empleado-update';

describe('Empleado Management Update Component', () => {
  let comp: EmpleadoUpdate;
  let fixture: ComponentFixture<EmpleadoUpdate>;
  let activatedRoute: ActivatedRoute;
  let empleadoFormService: EmpleadoFormService;
  let empleadoService: EmpleadoService;
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

    fixture = TestBed.createComponent(EmpleadoUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empleadoFormService = TestBed.inject(EmpleadoFormService);
    empleadoService = TestBed.inject(EmpleadoService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const empleado: IEmpleado = { id: 25035 };
      const usuario: IUser = { id: 3944 };
      empleado.usuario = usuario;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [usuario];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const empleado: IEmpleado = { id: 25035 };
      const usuario: IUser = { id: 3944 };
      empleado.usuario = usuario;

      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      expect(comp.usersSharedCollection()).toContainEqual(usuario);
      expect(comp.empleado).toEqual(empleado);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEmpleado>();
      const empleado = { id: 11214 };
      vi.spyOn(empleadoFormService, 'getEmpleado').mockReturnValue(empleado);
      vi.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(empleado);
      saveSubject.complete();

      // THEN
      expect(empleadoFormService.getEmpleado).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(empleadoService.update).toHaveBeenCalledWith(expect.objectContaining(empleado));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEmpleado>();
      const empleado = { id: 11214 };
      vi.spyOn(empleadoFormService, 'getEmpleado').mockReturnValue({ id: null });
      vi.spyOn(empleadoService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(empleado);
      saveSubject.complete();

      // THEN
      expect(empleadoFormService.getEmpleado).toHaveBeenCalled();
      expect(empleadoService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEmpleado>();
      const empleado = { id: 11214 };
      vi.spyOn(empleadoService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleado });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empleadoService.update).toHaveBeenCalled();
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
