import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, OnInit, Output, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { EntitySelectorComponent, EntitySelectorOption } from 'app/shared/primeng/entity-selector/entity-selector.component';
import { UserService } from '../../user/service/user.service';
import { IUser } from '../../user/user.model';

@Component({
  selector: 'jhi-empleado-ag-filter',
  templateUrl: './empleado-ag-filter.html',
  imports: [EntitySelectorComponent],
})
export class EmpleadoAgFilter implements OnInit {
  @Output() readonly filtersChange = new EventEmitter<Record<string, string[]>>();
  readonly usuarioOptions = signal<EntitySelectorOption<string | number>[]>([]);
  protected readonly usuarioService = inject(UserService);

  ngOnInit(): void {
    this.loadUsuarioOptions();
  }

  protected loadUsuarioOptions(): void {
    const request = this.usuarioService.query({ size: 10000 }) as Observable<HttpResponse<IUser[]>>;
    request.subscribe(response => {
      const options: EntitySelectorOption<string | number>[] = (response.body ?? []).map((related: IUser) => ({
        label: String(related.login ?? related.id),
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
    this.filtersChange.emit({ '0.contains': labels });
  }
}
