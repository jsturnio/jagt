import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPractica, NewPractica } from '../practica.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPractica for edit and NewPracticaFormGroupInput for create.
 */
type PracticaFormGroupInput = IPractica | PartialWithRequiredKeyOf<NewPractica>;

type PracticaFormDefaults = Pick<NewPractica, 'id'>;

type PracticaFormGroupContent = {
  id: FormControl<IPractica['id'] | NewPractica['id']>;
  cantidad: FormControl<IPractica['cantidad']>;
  pvalor: FormControl<IPractica['pvalor']>;
  prestacion: FormControl<IPractica['prestacion']>;
  orden: FormControl<IPractica['orden']>;
};

export type PracticaFormGroup = FormGroup<PracticaFormGroupContent>;

@Service()
export class PracticaFormService {
  createPracticaFormGroup(practica?: PracticaFormGroupInput): PracticaFormGroup {
    const practicaRawValue = {
      ...this.getFormDefaults(),
      ...(practica ?? { id: null }),
    };

    return new FormGroup<PracticaFormGroupContent>({
      id: new FormControl(
        { value: practicaRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cantidad: new FormControl(practicaRawValue.cantidad, {
        validators: [Validators.required, Validators.min(1)],
      }),
      pvalor: new FormControl(practicaRawValue.pvalor),
      prestacion: new FormControl(practicaRawValue.prestacion, {
        validators: [Validators.required],
      }),
      orden: new FormControl(practicaRawValue.orden, {
        validators: [Validators.required],
      }),
    });
  }

  getPractica(form: PracticaFormGroup): IPractica | NewPractica {
    return form.getRawValue();
  }

  resetForm(form: PracticaFormGroup, practica: PracticaFormGroupInput): void {
    const practicaRawValue = { ...this.getFormDefaults(), ...practica };
    form.reset({
      ...practicaRawValue,
      id: { value: practicaRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PracticaFormDefaults {
    return {
      id: null,
    };
  }
}
