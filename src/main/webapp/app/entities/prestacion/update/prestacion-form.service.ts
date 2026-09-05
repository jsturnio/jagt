import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPrestacion, NewPrestacion } from '../prestacion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPrestacion for edit and NewPrestacionFormGroupInput for create.
 */
type PrestacionFormGroupInput = IPrestacion | PartialWithRequiredKeyOf<NewPrestacion>;

type PrestacionFormDefaults = Pick<NewPrestacion, 'id' | 'reqAutorizacion'>;

type PrestacionFormGroupContent = {
  id: FormControl<IPrestacion['id'] | NewPrestacion['id']>;
  codigo: FormControl<IPrestacion['codigo']>;
  codigoInos: FormControl<IPrestacion['codigoInos']>;
  valorUb: FormControl<IPrestacion['valorUb']>;
  reqAutorizacion: FormControl<IPrestacion['reqAutorizacion']>;
  nomenclador: FormControl<IPrestacion['nomenclador']>;
};

export type PrestacionFormGroup = FormGroup<PrestacionFormGroupContent>;

@Service()
export class PrestacionFormService {
  createPrestacionFormGroup(prestacion?: PrestacionFormGroupInput): PrestacionFormGroup {
    const prestacionRawValue = {
      ...this.getFormDefaults(),
      ...(prestacion ?? { id: null }),
    };

    return new FormGroup<PrestacionFormGroupContent>({
      id: new FormControl(
        { value: prestacionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codigo: new FormControl(prestacionRawValue.codigo, {
        validators: [Validators.required],
      }),
      codigoInos: new FormControl(prestacionRawValue.codigoInos),
      valorUb: new FormControl(prestacionRawValue.valorUb),
      reqAutorizacion: new FormControl(prestacionRawValue.reqAutorizacion),
      nomenclador: new FormControl(prestacionRawValue.nomenclador, {
        validators: [Validators.required],
      }),
    });
  }

  getPrestacion(form: PrestacionFormGroup): IPrestacion | NewPrestacion {
    return form.getRawValue();
  }

  resetForm(form: PrestacionFormGroup, prestacion: PrestacionFormGroupInput): void {
    const prestacionRawValue = { ...this.getFormDefaults(), ...prestacion };
    form.reset({
      ...prestacionRawValue,
      id: { value: prestacionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PrestacionFormDefaults {
    return {
      id: null,
      reqAutorizacion: false,
    };
  }
}
