import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config';
import { IOrden, NewOrden } from '../orden.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrden for edit and NewOrdenFormGroupInput for create.
 */
type OrdenFormGroupInput = IOrden | PartialWithRequiredKeyOf<NewOrden>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrden | NewOrden> = Omit<T, 'fechaCreacion'> & {
  fechaCreacion?: string | null;
};

type OrdenFormRawValue = FormValueOf<IOrden>;

type NewOrdenFormRawValue = FormValueOf<NewOrden>;

type OrdenFormDefaults = Pick<NewOrden, 'id' | 'fechaCreacion'>;

type OrdenFormGroupContent = {
  id: FormControl<OrdenFormRawValue['id'] | NewOrden['id']>;
  afiliado: FormControl<OrdenFormRawValue['afiliado']>;
  protocolo: FormControl<OrdenFormRawValue['protocolo']>;
  fechaOrden: FormControl<OrdenFormRawValue['fechaOrden']>;
  fechaPrescripcion: FormControl<OrdenFormRawValue['fechaPrescripcion']>;
  totalOrden: FormControl<OrdenFormRawValue['totalOrden']>;
  sumaUb: FormControl<OrdenFormRawValue['sumaUb']>;
  fechaCreacion: FormControl<OrdenFormRawValue['fechaCreacion']>;
  paquete: FormControl<OrdenFormRawValue['paquete']>;
  usuario: FormControl<OrdenFormRawValue['usuario']>;
  bioquimico: FormControl<OrdenFormRawValue['bioquimico']>;
};

export type OrdenFormGroup = FormGroup<OrdenFormGroupContent>;

@Service()
export class OrdenFormService {
  createOrdenFormGroup(orden?: OrdenFormGroupInput): OrdenFormGroup {
    const ordenRawValue = this.convertOrdenToOrdenRawValue({
      ...this.getFormDefaults(),
      ...(orden ?? { id: null }),
    });

    return new FormGroup<OrdenFormGroupContent>({
      id: new FormControl(
        { value: ordenRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      afiliado: new FormControl(ordenRawValue.afiliado),
      protocolo: new FormControl(ordenRawValue.protocolo),
      fechaOrden: new FormControl(ordenRawValue.fechaOrden),
      fechaPrescripcion: new FormControl(ordenRawValue.fechaPrescripcion),
      totalOrden: new FormControl(ordenRawValue.totalOrden),
      sumaUb: new FormControl(ordenRawValue.sumaUb),
      fechaCreacion: new FormControl(ordenRawValue.fechaCreacion),
      paquete: new FormControl(ordenRawValue.paquete, {
        validators: [Validators.required],
      }),
      usuario: new FormControl(ordenRawValue.usuario, {
        validators: [Validators.required],
      }),
      bioquimico: new FormControl(ordenRawValue.bioquimico, {
        validators: [Validators.required],
      }),
    });
  }

  getOrden(form: OrdenFormGroup): IOrden | NewOrden {
    return this.convertOrdenRawValueToOrden(form.getRawValue());
  }

  resetForm(form: OrdenFormGroup, orden: OrdenFormGroupInput): void {
    const ordenRawValue = this.convertOrdenToOrdenRawValue({ ...this.getFormDefaults(), ...orden });
    form.reset({
      ...ordenRawValue,
      id: { value: ordenRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): OrdenFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaCreacion: currentTime,
    };
  }

  private convertOrdenRawValueToOrden(rawOrden: OrdenFormRawValue | NewOrdenFormRawValue): IOrden | NewOrden {
    return {
      ...rawOrden,
      fechaCreacion: dayjs(rawOrden.fechaCreacion, DATE_TIME_FORMAT),
    };
  }

  private convertOrdenToOrdenRawValue(
    orden: IOrden | (Partial<NewOrden> & OrdenFormDefaults),
  ): OrdenFormRawValue | PartialWithRequiredKeyOf<NewOrdenFormRawValue> {
    return {
      ...orden,
      fechaCreacion: orden.fechaCreacion ? orden.fechaCreacion.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
