import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { INomenclador, NewNomenclador } from '../nomenclador.model';

export type PartialUpdateNomenclador = Partial<INomenclador> & Pick<INomenclador, 'id'>;

@Service()
export class NomencladorsService {
  readonly nomencladorsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly nomencladorsResource = httpResource<INomenclador[]>(() => {
    const params = this.nomencladorsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of nomenclador that have been fetched. It is updated when the nomencladorsResource emits a new value.
   * In case of error while fetching the nomencladors, the signal is set to an empty array.
   */
  readonly nomencladors = computed(() => (this.nomencladorsResource.hasValue() ? this.nomencladorsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/nomencladors`;
}

@Service()
export class NomencladorService extends NomencladorsService {
  protected readonly http = inject(HttpClient);

  create(nomenclador: NewNomenclador): Observable<INomenclador> {
    return this.http.post<INomenclador>(this.resourceUrl, nomenclador);
  }

  update(nomenclador: INomenclador): Observable<INomenclador> {
    return this.http.put<INomenclador>(
      `${this.resourceUrl}/${encodeURIComponent(this.getNomencladorIdentifier(nomenclador))}`,
      nomenclador,
    );
  }

  partialUpdate(nomenclador: PartialUpdateNomenclador): Observable<INomenclador> {
    return this.http.patch<INomenclador>(
      `${this.resourceUrl}/${encodeURIComponent(this.getNomencladorIdentifier(nomenclador))}`,
      nomenclador,
    );
  }

  find(id: number): Observable<INomenclador> {
    return this.http.get<INomenclador>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<INomenclador[]>> {
    const options = createRequestOption(req);
    return this.http.get<INomenclador[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getNomencladorIdentifier(nomenclador: Pick<INomenclador, 'id'>): number {
    return nomenclador.id;
  }

  compareNomenclador(o1: Pick<INomenclador, 'id'> | null, o2: Pick<INomenclador, 'id'> | null): boolean {
    return o1 && o2 ? this.getNomencladorIdentifier(o1) === this.getNomencladorIdentifier(o2) : o1 === o2;
  }

  addNomencladorToCollectionIfMissing<Type extends Pick<INomenclador, 'id'>>(
    nomencladorCollection: Type[],
    ...nomencladorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const nomencladors: Type[] = nomencladorsToCheck.filter(nomencladorItem => nomencladorItem !== null && nomencladorItem !== undefined);
    if (nomencladors.length > 0) {
      const nomencladorCollectionIdentifiers = nomencladorCollection.map(nomencladorItem => this.getNomencladorIdentifier(nomencladorItem));
      const nomencladorsToAdd = nomencladors.filter(nomencladorItem => {
        const nomencladorIdentifier = this.getNomencladorIdentifier(nomencladorItem);
        if (nomencladorCollectionIdentifiers.includes(nomencladorIdentifier)) {
          return false;
        }
        nomencladorCollectionIdentifiers.push(nomencladorIdentifier);
        return true;
      });
      return [...nomencladorsToAdd, ...nomencladorCollection];
    }
    return nomencladorCollection;
  }
}
