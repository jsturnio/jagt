import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPlanMutual, NewPlanMutual } from '../plan-mutual.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlanMutual for edit and NewPlanMutualFormGroupInput for create.
 */
type PlanMutualFormGroupInput = IPlanMutual | PartialWithRequiredKeyOf<NewPlanMutual>;

type PlanMutualFormDefaults = Pick<NewPlanMutual, 'id' | 'prestacionesImportadas'>;

type PlanMutualFormGroupContent = {
  id: FormControl<IPlanMutual['id'] | NewPlanMutual['id']>;
  categoria: FormControl<IPlanMutual['categoria']>;
  etiquetaReporte: FormControl<IPlanMutual['etiquetaReporte']>;
  prestacionesImportadas: FormControl<IPlanMutual['prestacionesImportadas']>;
  mutual: FormControl<IPlanMutual['mutual']>;
};

export type PlanMutualFormGroup = FormGroup<PlanMutualFormGroupContent>;

@Service()
export class PlanMutualFormService {
  createPlanMutualFormGroup(planMutual?: PlanMutualFormGroupInput): PlanMutualFormGroup {
    const planMutualRawValue = {
      ...this.getFormDefaults(),
      ...(planMutual ?? { id: null }),
    };

    return new FormGroup<PlanMutualFormGroupContent>({
      id: new FormControl(
        { value: planMutualRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      categoria: new FormControl(planMutualRawValue.categoria, {
        validators: [Validators.required, Validators.maxLength(7)],
      }),
      etiquetaReporte: new FormControl(planMutualRawValue.etiquetaReporte, {
        validators: [Validators.required, Validators.maxLength(7)],
      }),
      prestacionesImportadas: new FormControl(planMutualRawValue.prestacionesImportadas),
      mutual: new FormControl(planMutualRawValue.mutual, {
        validators: [Validators.required],
      }),
    });
  }

  getPlanMutual(form: PlanMutualFormGroup): IPlanMutual | NewPlanMutual {
    return form.getRawValue();
  }

  resetForm(form: PlanMutualFormGroup, planMutual: PlanMutualFormGroupInput): void {
    const planMutualRawValue = { ...this.getFormDefaults(), ...planMutual };
    form.reset({
      ...planMutualRawValue,
      id: { value: planMutualRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PlanMutualFormDefaults {
    return {
      id: null,
      prestacionesImportadas: false,
    };
  }
}
