import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IOrden, NewOrden } from '../orden.model';

export type PartialUpdateOrden = Partial<IOrden> & Pick<IOrden, 'id'>;

type RestOf<T extends IOrden | NewOrden> = Omit<T, 'fechaOrden' | 'fechaPrescripcion' | 'fechaCreacion'> & {
  fechaOrden?: string | null;
  fechaPrescripcion?: string | null;
  fechaCreacion?: string | null;
};

export type RestOrden = RestOf<IOrden>;

export type NewRestOrden = RestOf<NewOrden>;

export type PartialUpdateRestOrden = RestOf<PartialUpdateOrden>;

@Service()
export class OrdensService {
  readonly ordensParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly ordensResource = httpResource<RestOrden[]>(() => {
    const params = this.ordensParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of orden that have been fetched. It is updated when the ordensResource emits a new value.
   * In case of error while fetching the ordens, the signal is set to an empty array.
   */
  readonly ordens = computed(() =>
    (this.ordensResource.hasValue() ? this.ordensResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/ordens`;

  protected convertValueFromServer(restOrden: RestOrden): IOrden {
    return {
      ...restOrden,
      fechaOrden: restOrden.fechaOrden ? dayjs(restOrden.fechaOrden) : undefined,
      fechaPrescripcion: restOrden.fechaPrescripcion ? dayjs(restOrden.fechaPrescripcion) : undefined,
      fechaCreacion: restOrden.fechaCreacion ? dayjs(restOrden.fechaCreacion) : undefined,
    };
  }
}

@Service()
export class OrdenService extends OrdensService {
  protected readonly http = inject(HttpClient);

  create(orden: NewOrden): Observable<IOrden> {
    const copy = this.convertValueFromClient(orden);
    return this.http.post<RestOrden>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(orden: IOrden): Observable<IOrden> {
    const copy = this.convertValueFromClient(orden);
    return this.http
      .put<RestOrden>(`${this.resourceUrl}/${encodeURIComponent(this.getOrdenIdentifier(orden))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(orden: PartialUpdateOrden): Observable<IOrden> {
    const copy = this.convertValueFromClient(orden);
    return this.http
      .patch<RestOrden>(`${this.resourceUrl}/${encodeURIComponent(this.getOrdenIdentifier(orden))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IOrden> {
    return this.http.get<RestOrden>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IOrden[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrden[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getOrdenIdentifier(orden: Pick<IOrden, 'id'>): number {
    return orden.id;
  }

  compareOrden(o1: Pick<IOrden, 'id'> | null, o2: Pick<IOrden, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrdenIdentifier(o1) === this.getOrdenIdentifier(o2) : o1 === o2;
  }

  addOrdenToCollectionIfMissing<Type extends Pick<IOrden, 'id'>>(
    ordenCollection: Type[],
    ...ordensToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ordens: Type[] = ordensToCheck.filter(ordenItem => ordenItem !== null && ordenItem !== undefined);
    if (ordens.length > 0) {
      const ordenCollectionIdentifiers = ordenCollection.map(ordenItem => this.getOrdenIdentifier(ordenItem));
      const ordensToAdd = ordens.filter(ordenItem => {
        const ordenIdentifier = this.getOrdenIdentifier(ordenItem);
        if (ordenCollectionIdentifiers.includes(ordenIdentifier)) {
          return false;
        }
        ordenCollectionIdentifiers.push(ordenIdentifier);
        return true;
      });
      return [...ordensToAdd, ...ordenCollection];
    }
    return ordenCollection;
  }

  protected convertValueFromClient<T extends IOrden | NewOrden | PartialUpdateOrden>(orden: T): RestOf<T> {
    return {
      ...orden,
      fechaOrden: orden.fechaOrden?.format(DATE_FORMAT) ?? null,
      fechaPrescripcion: orden.fechaPrescripcion?.format(DATE_FORMAT) ?? null,
      fechaCreacion: orden.fechaCreacion?.toJSON() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestOrden): IOrden {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestOrden[]): IOrden[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
