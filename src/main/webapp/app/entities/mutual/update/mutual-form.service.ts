import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMutual, NewMutual } from '../mutual.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMutual for edit and NewMutualFormGroupInput for create.
 */
type MutualFormGroupInput = IMutual | PartialWithRequiredKeyOf<NewMutual>;

type MutualFormDefaults = Pick<NewMutual, 'id' | 'habilitada'>;

type MutualFormGroupContent = {
  id: FormControl<IMutual['id'] | NewMutual['id']>;
  nombre: FormControl<IMutual['nombre']>;
  descripcion: FormControl<IMutual['descripcion']>;
  cuit: FormControl<IMutual['cuit']>;
  tipoIva: FormControl<IMutual['tipoIva']>;
  domicilio: FormControl<IMutual['domicilio']>;
  telefono: FormControl<IMutual['telefono']>;
  email: FormControl<IMutual['email']>;
  habilitada: FormControl<IMutual['habilitada']>;
};

export type MutualFormGroup = FormGroup<MutualFormGroupContent>;

@Service()
export class MutualFormService {
  createMutualFormGroup(mutual?: MutualFormGroupInput): MutualFormGroup {
    const mutualRawValue = {
      ...this.getFormDefaults(),
      ...(mutual ?? { id: null }),
    };

    return new FormGroup<MutualFormGroupContent>({
      id: new FormControl(
        { value: mutualRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(mutualRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(7)],
      }),
      descripcion: new FormControl(mutualRawValue.descripcion, {
        validators: [Validators.maxLength(100)],
      }),
      cuit: new FormControl(mutualRawValue.cuit, {
        validators: [Validators.maxLength(20)],
      }),
      tipoIva: new FormControl(mutualRawValue.tipoIva, {
        validators: [Validators.required],
      }),
      domicilio: new FormControl(mutualRawValue.domicilio, {
        validators: [Validators.maxLength(50)],
      }),
      telefono: new FormControl(mutualRawValue.telefono, {
        validators: [
          Validators.pattern('^(\\d{3}[- .]?){2}\\d{4}$'), // NOSONAR
        ],
      }),
      email: new FormControl(mutualRawValue.email, {
        validators: [
          Validators.pattern('^(.+)@(\\S+)$'), // NOSONAR
        ],
      }),
      habilitada: new FormControl(mutualRawValue.habilitada),
    });
  }

  getMutual(form: MutualFormGroup): IMutual | NewMutual {
    return form.getRawValue();
  }

  resetForm(form: MutualFormGroup, mutual: MutualFormGroupInput): void {
    const mutualRawValue = { ...this.getFormDefaults(), ...mutual };
    form.reset({
      ...mutualRawValue,
      id: { value: mutualRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): MutualFormDefaults {
    return {
      id: null,
      habilitada: false,
    };
  }
}
