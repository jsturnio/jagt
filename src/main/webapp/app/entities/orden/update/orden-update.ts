import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { IBioquimico } from 'app/entities/bioquimico/bioquimico.model';
import { BioquimicoService } from 'app/entities/bioquimico/service/bioquimico.service';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { IPaquete } from 'app/entities/paquete/paquete.model';
import { PaqueteService } from 'app/entities/paquete/service/paquete.service';
import { AlertError } from 'app/shared/alert';
import { IOrden } from '../orden.model';
import { OrdenService } from '../service/orden.service';

import { OrdenFormGroup, OrdenFormService } from './orden-form.service';

@Component({
  selector: 'jhi-orden-update',
  templateUrl: './orden-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class OrdenUpdate implements OnInit {
  readonly isSaving = signal(false);
  orden: IOrden | null = null;

  paquetesSharedCollection = signal<IPaquete[]>([]);
  empleadosSharedCollection = signal<IEmpleado[]>([]);
  bioquimicosSharedCollection = signal<IBioquimico[]>([]);

  protected ordenService = inject(OrdenService);
  protected ordenFormService = inject(OrdenFormService);
  protected paqueteService = inject(PaqueteService);
  protected empleadoService = inject(EmpleadoService);
  protected bioquimicoService = inject(BioquimicoService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: OrdenFormGroup = this.ordenFormService.createOrdenFormGroup();

  comparePaquete = (o1: IPaquete | null, o2: IPaquete | null): boolean => this.paqueteService.comparePaquete(o1, o2);

  compareEmpleado = (o1: IEmpleado | null, o2: IEmpleado | null): boolean => this.empleadoService.compareEmpleado(o1, o2);

  compareBioquimico = (o1: IBioquimico | null, o2: IBioquimico | null): boolean => this.bioquimicoService.compareBioquimico(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orden }) => {
      this.orden = orden;
      if (orden) {
        this.updateForm(orden);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const orden = this.ordenFormService.getOrden(this.editForm);
    if (orden.id === null) {
      this.subscribeToSaveResponse(this.ordenService.create(orden));
    } else {
      this.subscribeToSaveResponse(this.ordenService.update(orden));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IOrden | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(orden: IOrden): void {
    this.orden = orden;
    this.ordenFormService.resetForm(this.editForm, orden);

    this.paquetesSharedCollection.update(paquetes =>
      this.paqueteService.addPaqueteToCollectionIfMissing<IPaquete>(paquetes, orden.paquete),
    );
    this.empleadosSharedCollection.update(empleados =>
      this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(empleados, orden.usuario),
    );
    this.bioquimicosSharedCollection.update(bioquimicos =>
      this.bioquimicoService.addBioquimicoToCollectionIfMissing<IBioquimico>(bioquimicos, orden.bioquimico),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.paqueteService
      .query()
      .pipe(map((res: HttpResponse<IPaquete[]>) => res.body ?? []))
      .pipe(map((paquetes: IPaquete[]) => this.paqueteService.addPaqueteToCollectionIfMissing<IPaquete>(paquetes, this.orden?.paquete)))
      .subscribe((paquetes: IPaquete[]) => this.paquetesSharedCollection.set(paquetes));

    this.empleadoService
      .query()
      .pipe(map((res: HttpResponse<IEmpleado[]>) => res.body ?? []))
      .pipe(
        map((empleados: IEmpleado[]) => this.empleadoService.addEmpleadoToCollectionIfMissing<IEmpleado>(empleados, this.orden?.usuario)),
      )
      .subscribe((empleados: IEmpleado[]) => this.empleadosSharedCollection.set(empleados));

    this.bioquimicoService
      .query()
      .pipe(map((res: HttpResponse<IBioquimico[]>) => res.body ?? []))
      .pipe(
        map((bioquimicos: IBioquimico[]) =>
          this.bioquimicoService.addBioquimicoToCollectionIfMissing<IBioquimico>(bioquimicos, this.orden?.bioquimico),
        ),
      )
      .subscribe((bioquimicos: IBioquimico[]) => this.bioquimicosSharedCollection.set(bioquimicos));
  }
}
