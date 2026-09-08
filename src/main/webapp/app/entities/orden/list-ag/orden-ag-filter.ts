import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { EntitySelectorComponent, EntitySelectorOption } from 'app/shared/primeng/entity-selector/entity-selector.component';
import { IBioquimico } from '../../bioquimico/bioquimico.model';
import { BioquimicoService } from '../../bioquimico/service/bioquimico.service';
import { IEmpleado } from '../../empleado/empleado.model';
import { EmpleadoService } from '../../empleado/service/empleado.service';
import { IPaquete } from '../../paquete/paquete.model';
import { PaqueteService } from '../../paquete/service/paquete.service';

@Component({
  selector: 'jhi-orden-ag-filter',
  templateUrl: './orden-ag-filter.html',
  imports: [EntitySelectorComponent],
})
export class OrdenAgFilter implements OnInit {
  @Output() readonly filtersChange = new EventEmitter<Record<string, string[]>>();
  readonly paqueteOptions = signal<EntitySelectorOption<string | number>[]>([]);
  protected readonly paqueteService = inject(PaqueteService);
  readonly usuarioOptions = signal<EntitySelectorOption<string | number>[]>([]);
  protected readonly usuarioService = inject(EmpleadoService);
  readonly bioquimicoOptions = signal<EntitySelectorOption<string | number>[]>([]);
  protected readonly bioquimicoService = inject(BioquimicoService);

  ngOnInit(): void {
    this.loadPaqueteOptions();
    this.loadUsuarioOptions();
    this.loadBioquimicoOptions();
  }

  protected loadPaqueteOptions(): void {
    const request = this.paqueteService.query({ size: 10000 }) as Observable<HttpResponse<IPaquete[]>>;
    request.subscribe(response => {
      const options: EntitySelectorOption<string | number>[] = (response.body ?? []).map((related: IPaquete) => ({
        label: String(related.paqDescrip ?? related.id),
        value: related.id as string | number,
      }));
      this.paqueteOptions.set([...new Map(options.map(option => [option.value, option])).values()]);
    });
  }

  onPaqueteChange(value: string | number | (string | number)[] | null): void {
    const values = Array.isArray(value) ? value : value == null ? [] : [value];
    const labels = values
      .map(selected => this.paqueteOptions().find(option => String(option.value) === String(selected))?.label)
      .filter((label): label is string => label != null);
    this.filtersChange.emit({ '0.contains': labels });
  }
  protected loadUsuarioOptions(): void {
    const request = this.usuarioService.query({ size: 10000 }) as Observable<HttpResponse<IEmpleado[]>>;
    request.subscribe(response => {
      const options: EntitySelectorOption<string | number>[] = (response.body ?? []).map((related: IEmpleado) => ({
        label: String(related.id ?? related.id),
        value: related.id as string | number,
      }));
      this.usuarioOptions.set([...new Map(options.map(option => [option.value, option])).values()]);
    });
  }

  onUsuarioChange(value: string | number | (string | number)[] | null): void {
    const values = Array.isArray(value) ? value : value == null ? [] : [value];
    const labels = values
      .map(selected => this.usuarioOptions().find(option => String(option.value) === String(selected))?.label)
      .filter((label): label is string => label != null);
    this.filtersChange.emit({ '1.contains': labels });
  }
  protected loadBioquimicoOptions(): void {
    const request = this.bioquimicoService.query({ size: 10000 }) as Observable<HttpResponse<IBioquimico[]>>;
    request.subscribe(response => {
      const options: EntitySelectorOption<string | number>[] = (response.body ?? []).map((related: IBioquimico) => ({
        label: String(related.nombreCompleto ?? related.id),
        value: related.id as string | number,
      }));
      this.bioquimicoOptions.set([...new Map(options.map(option => [option.value, option])).values()]);
    });
  }

  onBioquimicoChange(value: string | number | (string | number)[] | null): void {
    const values = Array.isArray(value) ? value : value == null ? [] : [value];
    const labels = values
      .map(selected => this.bioquimicoOptions().find(option => String(option.value) === String(selected))?.label)
      .filter((label): label is string => label != null);
    this.filtersChange.emit({ '2.contains': labels });
  }
}
