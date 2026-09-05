import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEmpleado, NewEmpleado } from '../empleado.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmpleado for edit and NewEmpleadoFormGroupInput for create.
 */
type EmpleadoFormGroupInput = IEmpleado | PartialWithRequiredKeyOf<NewEmpleado>;

type EmpleadoFormDefaults = Pick<NewEmpleado, 'id'>;

type EmpleadoFormGroupContent = {
  id: FormControl<IEmpleado['id'] | NewEmpleado['id']>;
  nombre: FormControl<IEmpleado['nombre']>;
  apellido: FormControl<IEmpleado['apellido']>;
  cuit: FormControl<IEmpleado['cuit']>;
  tipoIva: FormControl<IEmpleado['tipoIva']>;
  domicilio: FormControl<IEmpleado['domicilio']>;
  email: FormControl<IEmpleado['email']>;
  fechaIngreso: FormControl<IEmpleado['fechaIngreso']>;
  telefono: FormControl<IEmpleado['telefono']>;
  genero: FormControl<IEmpleado['genero']>;
  usuario: FormControl<IEmpleado['usuario']>;
};

export type EmpleadoFormGroup = FormGroup<EmpleadoFormGroupContent>;

@Service()
export class EmpleadoFormService {
  createEmpleadoFormGroup(empleado?: EmpleadoFormGroupInput): EmpleadoFormGroup {
    const empleadoRawValue = {
      ...this.getFormDefaults(),
      ...(empleado ?? { id: null }),
    };

    return new FormGroup<EmpleadoFormGroupContent>({
      id: new FormControl(
        { value: empleadoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nombre: new FormControl(empleadoRawValue.nombre),
      apellido: new FormControl(empleadoRawValue.apellido),
      cuit: new FormControl(empleadoRawValue.cuit),
      tipoIva: new FormControl(empleadoRawValue.tipoIva, {
        validators: [Validators.required],
      }),
      domicilio: new FormControl(empleadoRawValue.domicilio),
      email: new FormControl(empleadoRawValue.email, {
        validators: [
          Validators.required,
          Validators.pattern('^(.+)@(\\S+)$'), // NOSONAR
        ],
      }),
      fechaIngreso: new FormControl(empleadoRawValue.fechaIngreso),
      telefono: new FormControl(empleadoRawValue.telefono, {
        validators: [
          Validators.pattern('^(\\d{3}[- .]?){2}\\d{3}$'), // NOSONAR
        ],
      }),
      genero: new FormControl(empleadoRawValue.genero, {
        validators: [Validators.required],
      }),
      usuario: new FormControl(empleadoRawValue.usuario, {
        validators: [Validators.required],
      }),
    });
  }

  getEmpleado(form: EmpleadoFormGroup): IEmpleado | NewEmpleado {
    return form.getRawValue();
  }

  resetForm(form: EmpleadoFormGroup, empleado: EmpleadoFormGroupInput): void {
    const empleadoRawValue = { ...this.getFormDefaults(), ...empleado };
    form.reset({
      ...empleadoRawValue,
      id: { value: empleadoRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EmpleadoFormDefaults {
    return {
      id: null,
    };
  }
}
