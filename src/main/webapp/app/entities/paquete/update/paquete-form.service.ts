import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPaquete, NewPaquete } from '../paquete.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPaquete for edit and NewPaqueteFormGroupInput for create.
 */
type PaqueteFormGroupInput = IPaquete | PartialWithRequiredKeyOf<NewPaquete>;

type PaqueteFormDefaults = Pick<NewPaquete, 'id'>;

type PaqueteFormGroupContent = {
  id: FormControl<IPaquete['id'] | NewPaquete['id']>;
  paqDescrip: FormControl<IPaquete['paqDescrip']>;
  nombre: FormControl<IPaquete['nombre']>;
  periodo: FormControl<IPaquete['periodo']>;
  estado: FormControl<IPaquete['estado']>;
  descripcion: FormControl<IPaquete['descripcion']>;
  tipoIva: FormControl<IPaquete['tipoIva']>;
  plan: FormControl<IPaquete['plan']>;
};

export type PaqueteFormGroup = FormGroup<PaqueteFormGroupContent>;

@Service()
export class PaqueteFormService {
  createPaqueteFormGroup(paquete?: PaqueteFormGroupInput): PaqueteFormGroup {
    const paqueteRawValue = {
      ...this.getFormDefaults(),
      ...(paquete ?? { id: null }),
    };

    return new FormGroup<PaqueteFormGroupContent>({
      id: new FormControl(
        { value: paqueteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      paqDescrip: new FormControl(paqueteRawValue.paqDescrip),
      nombre: new FormControl(paqueteRawValue.nombre),
      periodo: new FormControl(paqueteRawValue.periodo, {
        validators: [Validators.required],
      }),
      estado: new FormControl(paqueteRawValue.estado),
      descripcion: new FormControl(paqueteRawValue.descripcion, {
        validators: [Validators.maxLength(60)],
      }),
      tipoIva: new FormControl(paqueteRawValue.tipoIva),
      plan: new FormControl(paqueteRawValue.plan, {
        validators: [Validators.required],
      }),
    });
  }

  getPaquete(form: PaqueteFormGroup): IPaquete | NewPaquete {
    return form.getRawValue();
  }

  resetForm(form: PaqueteFormGroup, paquete: PaqueteFormGroupInput): void {
    const paqueteRawValue = { ...this.getFormDefaults(), ...paquete };
    form.reset({
      ...paqueteRawValue,
      id: { value: paqueteRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PaqueteFormDefaults {
    return {
      id: null,
    };
  }
}
