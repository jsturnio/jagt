import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPractica, NewPractica } from '../practica.model';

export type PartialUpdatePractica = Partial<IPractica> & Pick<IPractica, 'id'>;

@Service()
export class PracticasService {
  readonly practicasParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly practicasResource = httpResource<IPractica[]>(() => {
    const params = this.practicasParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of practica that have been fetched. It is updated when the practicasResource emits a new value.
   * In case of error while fetching the practicas, the signal is set to an empty array.
   */
  readonly practicas = computed(() => (this.practicasResource.hasValue() ? this.practicasResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/practicas`;
}

@Service()
export class PracticaService extends PracticasService {
  protected readonly http = inject(HttpClient);

  create(practica: NewPractica): Observable<IPractica> {
    return this.http.post<IPractica>(this.resourceUrl, practica);
  }

  update(practica: IPractica): Observable<IPractica> {
    return this.http.put<IPractica>(`${this.resourceUrl}/${encodeURIComponent(this.getPracticaIdentifier(practica))}`, practica);
  }

  partialUpdate(practica: PartialUpdatePractica): Observable<IPractica> {
    return this.http.patch<IPractica>(`${this.resourceUrl}/${encodeURIComponent(this.getPracticaIdentifier(practica))}`, practica);
  }

  find(id: number): Observable<IPractica> {
    return this.http.get<IPractica>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPractica[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPractica[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPracticaIdentifier(practica: Pick<IPractica, 'id'>): number {
    return practica.id;
  }

  comparePractica(o1: Pick<IPractica, 'id'> | null, o2: Pick<IPractica, 'id'> | null): boolean {
    return o1 && o2 ? this.getPracticaIdentifier(o1) === this.getPracticaIdentifier(o2) : o1 === o2;
  }

  addPracticaToCollectionIfMissing<Type extends Pick<IPractica, 'id'>>(
    practicaCollection: Type[],
    ...practicasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const practicas: Type[] = practicasToCheck.filter(practicaItem => practicaItem !== null && practicaItem !== undefined);
    if (practicas.length > 0) {
      const practicaCollectionIdentifiers = practicaCollection.map(practicaItem => this.getPracticaIdentifier(practicaItem));
      const practicasToAdd = practicas.filter(practicaItem => {
        const practicaIdentifier = this.getPracticaIdentifier(practicaItem);
        if (practicaCollectionIdentifiers.includes(practicaIdentifier)) {
          return false;
        }
        practicaCollectionIdentifiers.push(practicaIdentifier);
        return true;
      });
      return [...practicasToAdd, ...practicaCollection];
    }
    return practicaCollection;
  }
}
