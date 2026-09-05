import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPrestacion, NewPrestacion } from '../prestacion.model';

export type PartialUpdatePrestacion = Partial<IPrestacion> & Pick<IPrestacion, 'id'>;

@Service()
export class PrestacionsService {
  readonly prestacionsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly prestacionsResource = httpResource<IPrestacion[]>(() => {
    const params = this.prestacionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of prestacion that have been fetched. It is updated when the prestacionsResource emits a new value.
   * In case of error while fetching the prestacions, the signal is set to an empty array.
   */
  readonly prestacions = computed(() => (this.prestacionsResource.hasValue() ? this.prestacionsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/prestacions`;
}

@Service()
export class PrestacionService extends PrestacionsService {
  protected readonly http = inject(HttpClient);

  create(prestacion: NewPrestacion): Observable<IPrestacion> {
    return this.http.post<IPrestacion>(this.resourceUrl, prestacion);
  }

  update(prestacion: IPrestacion): Observable<IPrestacion> {
    return this.http.put<IPrestacion>(`${this.resourceUrl}/${encodeURIComponent(this.getPrestacionIdentifier(prestacion))}`, prestacion);
  }

  partialUpdate(prestacion: PartialUpdatePrestacion): Observable<IPrestacion> {
    return this.http.patch<IPrestacion>(`${this.resourceUrl}/${encodeURIComponent(this.getPrestacionIdentifier(prestacion))}`, prestacion);
  }

  find(id: number): Observable<IPrestacion> {
    return this.http.get<IPrestacion>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPrestacion[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPrestacion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPrestacionIdentifier(prestacion: Pick<IPrestacion, 'id'>): number {
    return prestacion.id;
  }

  comparePrestacion(o1: Pick<IPrestacion, 'id'> | null, o2: Pick<IPrestacion, 'id'> | null): boolean {
    return o1 && o2 ? this.getPrestacionIdentifier(o1) === this.getPrestacionIdentifier(o2) : o1 === o2;
  }

  addPrestacionToCollectionIfMissing<Type extends Pick<IPrestacion, 'id'>>(
    prestacionCollection: Type[],
    ...prestacionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const prestacions: Type[] = prestacionsToCheck.filter(prestacionItem => prestacionItem !== null && prestacionItem !== undefined);
    if (prestacions.length > 0) {
      const prestacionCollectionIdentifiers = prestacionCollection.map(prestacionItem => this.getPrestacionIdentifier(prestacionItem));
      const prestacionsToAdd = prestacions.filter(prestacionItem => {
        const prestacionIdentifier = this.getPrestacionIdentifier(prestacionItem);
        if (prestacionCollectionIdentifiers.includes(prestacionIdentifier)) {
          return false;
        }
        prestacionCollectionIdentifiers.push(prestacionIdentifier);
        return true;
      });
      return [...prestacionsToAdd, ...prestacionCollection];
    }
    return prestacionCollection;
  }
}
