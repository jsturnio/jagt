import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IEmpleado, NewEmpleado } from '../empleado.model';

export type PartialUpdateEmpleado = Partial<IEmpleado> & Pick<IEmpleado, 'id'>;

type RestOf<T extends IEmpleado | NewEmpleado> = Omit<T, 'fechaIngreso'> & {
  fechaIngreso?: string | null;
};

export type RestEmpleado = RestOf<IEmpleado>;

export type NewRestEmpleado = RestOf<NewEmpleado>;

export type PartialUpdateRestEmpleado = RestOf<PartialUpdateEmpleado>;

@Service()
export class EmpleadosService {
  readonly empleadosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly empleadosResource = httpResource<RestEmpleado[]>(() => {
    const params = this.empleadosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of empleado that have been fetched. It is updated when the empleadosResource emits a new value.
   * In case of error while fetching the empleados, the signal is set to an empty array.
   */
  readonly empleados = computed(() =>
    (this.empleadosResource.hasValue() ? this.empleadosResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/empleados`;

  protected convertValueFromServer(restEmpleado: RestEmpleado): IEmpleado {
    return {
      ...restEmpleado,
      fechaIngreso: restEmpleado.fechaIngreso ? dayjs(restEmpleado.fechaIngreso) : undefined,
    };
  }
}

@Service()
export class EmpleadoService extends EmpleadosService {
  protected readonly http = inject(HttpClient);

  create(empleado: NewEmpleado): Observable<IEmpleado> {
    const copy = this.convertValueFromClient(empleado);
    return this.http.post<RestEmpleado>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(empleado: IEmpleado): Observable<IEmpleado> {
    const copy = this.convertValueFromClient(empleado);
    return this.http
      .put<RestEmpleado>(`${this.resourceUrl}/${encodeURIComponent(this.getEmpleadoIdentifier(empleado))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(empleado: PartialUpdateEmpleado): Observable<IEmpleado> {
    const copy = this.convertValueFromClient(empleado);
    return this.http
      .patch<RestEmpleado>(`${this.resourceUrl}/${encodeURIComponent(this.getEmpleadoIdentifier(empleado))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IEmpleado> {
    return this.http
      .get<RestEmpleado>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IEmpleado[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEmpleado[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEmpleadoIdentifier(empleado: Pick<IEmpleado, 'id'>): number {
    return empleado.id;
  }

  compareEmpleado(o1: Pick<IEmpleado, 'id'> | null, o2: Pick<IEmpleado, 'id'> | null): boolean {
    return o1 && o2 ? this.getEmpleadoIdentifier(o1) === this.getEmpleadoIdentifier(o2) : o1 === o2;
  }

  addEmpleadoToCollectionIfMissing<Type extends Pick<IEmpleado, 'id'>>(
    empleadoCollection: Type[],
    ...empleadosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const empleados: Type[] = empleadosToCheck.filter(empleadoItem => empleadoItem !== null && empleadoItem !== undefined);
    if (empleados.length > 0) {
      const empleadoCollectionIdentifiers = empleadoCollection.map(empleadoItem => this.getEmpleadoIdentifier(empleadoItem));
      const empleadosToAdd = empleados.filter(empleadoItem => {
        const empleadoIdentifier = this.getEmpleadoIdentifier(empleadoItem);
        if (empleadoCollectionIdentifiers.includes(empleadoIdentifier)) {
          return false;
        }
        empleadoCollectionIdentifiers.push(empleadoIdentifier);
        return true;
      });
      return [...empleadosToAdd, ...empleadoCollection];
    }
    return empleadoCollection;
  }

  protected convertValueFromClient<T extends IEmpleado | NewEmpleado | PartialUpdateEmpleado>(empleado: T): RestOf<T> {
    return {
      ...empleado,
      fechaIngreso: empleado.fechaIngreso?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestEmpleado): IEmpleado {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestEmpleado[]): IEmpleado[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
