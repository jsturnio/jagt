import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { INomenclador, NewNomenclador } from '../nomenclador.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts INomenclador for edit and NewNomencladorFormGroupInput for create.
 */
type NomencladorFormGroupInput = INomenclador | PartialWithRequiredKeyOf<NewNomenclador>;

type NomencladorFormDefaults = Pick<NewNomenclador, 'id'>;

type NomencladorFormGroupContent = {
  id: FormControl<INomenclador['id'] | NewNomenclador['id']>;
  nombre: FormControl<INomenclador['nombre']>;
  descripcion: FormControl<INomenclador['descripcion']>;
  tipo: FormControl<INomenclador['tipo']>;
  mutual: FormControl<INomenclador['mutual']>;
};

export type NomencladorFormGroup = FormGroup<NomencladorFormGroupContent>;

@Service()
export class NomencladorFormService {
  createNomencladorFormGroup(nomenclador?: NomencladorFormGroupInput): NomencladorFormGroup {
    const nomencladorRawValue = {
      ...this.getFormDefaults(),
      ...(nomenclador ?? { id: null }),
    };

    return new FormGroup<NomencladorFormGroupContent>({
      id: new FormControl(
        { value: nomencladorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(nomencladorRawValue.nombre, {
        validators: [Validators.required, Validators.maxLength(12)],
      }),
      descripcion: new FormControl(nomencladorRawValue.descripcion, {
        validators: [Validators.maxLength(100)],
      }),
      tipo: new FormControl(nomencladorRawValue.tipo, {
        validators: [Validators.required],
      }),
      mutual: new FormControl(nomencladorRawValue.mutual, {
        validators: [Validators.required],
      }),
    });
  }

  getNomenclador(form: NomencladorFormGroup): INomenclador | NewNomenclador {
    return form.getRawValue();
  }

  resetForm(form: NomencladorFormGroup, nomenclador: NomencladorFormGroupInput): void {
    const nomencladorRawValue = { ...this.getFormDefaults(), ...nomenclador };
    form.reset({
      ...nomencladorRawValue,
      id: { value: nomencladorRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): NomencladorFormDefaults {
    return {
      id: null,
    };
  }
}
