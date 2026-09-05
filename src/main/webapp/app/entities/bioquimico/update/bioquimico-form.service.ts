import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBioquimico, NewBioquimico } from '../bioquimico.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBioquimico for edit and NewBioquimicoFormGroupInput for create.
 */
type BioquimicoFormGroupInput = IBioquimico | PartialWithRequiredKeyOf<NewBioquimico>;

type BioquimicoFormDefaults = Pick<NewBioquimico, 'id'>;

type BioquimicoFormGroupContent = {
  id: FormControl<IBioquimico['id'] | NewBioquimico['id']>;
  matricula: FormControl<IBioquimico['matricula']>;
  cuit: FormControl<IBioquimico['cuit']>;
  tipoIva: FormControl<IBioquimico['tipoIva']>;
  domicilioProfesional: FormControl<IBioquimico['domicilioProfesional']>;
  estado: FormControl<IBioquimico['estado']>;
  email: FormControl<IBioquimico['email']>;
  fechaIngreso: FormControl<IBioquimico['fechaIngreso']>;
  telefono: FormControl<IBioquimico['telefono']>;
  nombreEnDosep: FormControl<IBioquimico['nombreEnDosep']>;
  nroPrestadorOsde: FormControl<IBioquimico['nroPrestadorOsde']>;
  ingBrutos: FormControl<IBioquimico['ingBrutos']>;
  nroJubilacion: FormControl<IBioquimico['nroJubilacion']>;
  nroLaboratorio: FormControl<IBioquimico['nroLaboratorio']>;
  nombreCompleto: FormControl<IBioquimico['nombreCompleto']>;
  genero: FormControl<IBioquimico['genero']>;
  user: FormControl<IBioquimico['user']>;
};

export type BioquimicoFormGroup = FormGroup<BioquimicoFormGroupContent>;

@Service()
export class BioquimicoFormService {
  createBioquimicoFormGroup(bioquimico?: BioquimicoFormGroupInput): BioquimicoFormGroup {
    const bioquimicoRawValue = {
      ...this.getFormDefaults(),
      ...(bioquimico ?? { id: null }),
    };

    return new FormGroup<BioquimicoFormGroupContent>({
      id: new FormControl(
        { value: bioquimicoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      matricula: new FormControl(bioquimicoRawValue.matricula),
      cuit: new FormControl(bioquimicoRawValue.cuit),
      tipoIva: new FormControl(bioquimicoRawValue.tipoIva, {
        validators: [Validators.required],
      }),
      domicilioProfesional: new FormControl(bioquimicoRawValue.domicilioProfesional),
      estado: new FormControl(bioquimicoRawValue.estado),
      email: new FormControl(bioquimicoRawValue.email, {
        validators: [
          Validators.required,
          Validators.pattern('^(.+)@(\\S+)$'), // NOSONAR
        ],
      }),
      fechaIngreso: new FormControl(bioquimicoRawValue.fechaIngreso),
      telefono: new FormControl(bioquimicoRawValue.telefono, {
        validators: [
          Validators.pattern('^(\\d{3}[- .]?){2}\\d{3}$'), // NOSONAR
        ],
      }),
      nombreEnDosep: new FormControl(bioquimicoRawValue.nombreEnDosep),
      nroPrestadorOsde: new FormControl(bioquimicoRawValue.nroPrestadorOsde),
      ingBrutos: new FormControl(bioquimicoRawValue.ingBrutos, {
        validators: [Validators.maxLength(20)],
      }),
      nroJubilacion: new FormControl(bioquimicoRawValue.nroJubilacion),
      nroLaboratorio: new FormControl(bioquimicoRawValue.nroLaboratorio),
      nombreCompleto: new FormControl(bioquimicoRawValue.nombreCompleto),
      genero: new FormControl(bioquimicoRawValue.genero, {
        validators: [Validators.required],
      }),
      user: new FormControl(bioquimicoRawValue.user, {
        validators: [Validators.required],
      }),
    });
  }

  getBioquimico(form: BioquimicoFormGroup): IBioquimico | NewBioquimico {
    return form.getRawValue();
  }

  resetForm(form: BioquimicoFormGroup, bioquimico: BioquimicoFormGroupInput): void {
    const bioquimicoRawValue = { ...this.getFormDefaults(), ...bioquimico };
    form.reset({
      ...bioquimicoRawValue,
      id: { value: bioquimicoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): BioquimicoFormDefaults {
    return {
      id: null,
    };
  }
}
