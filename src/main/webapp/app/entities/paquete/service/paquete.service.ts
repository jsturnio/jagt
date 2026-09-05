import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPaquete, NewPaquete } from '../paquete.model';

export type PartialUpdatePaquete = Partial<IPaquete> & Pick<IPaquete, 'id'>;

type RestOf<T extends IPaquete | NewPaquete> = Omit<T, 'periodo'> & {
  periodo?: string | null;
};

export type RestPaquete = RestOf<IPaquete>;

export type NewRestPaquete = RestOf<NewPaquete>;

export type PartialUpdateRestPaquete = RestOf<PartialUpdatePaquete>;

@Service()
export class PaquetesService {
  readonly paquetesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly paquetesResource = httpResource<RestPaquete[]>(() => {
    const params = this.paquetesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of paquete that have been fetched. It is updated when the paquetesResource emits a new value.
   * In case of error while fetching the paquetes, the signal is set to an empty array.
   */
  readonly paquetes = computed(() =>
    (this.paquetesResource.hasValue() ? this.paquetesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/paquetes`;

  protected convertValueFromServer(restPaquete: RestPaquete): IPaquete {
    return {
      ...restPaquete,
      periodo: restPaquete.periodo ? dayjs(restPaquete.periodo) : undefined,
    };
  }
}

@Service()
export class PaqueteService extends PaquetesService {
  protected readonly http = inject(HttpClient);

  create(paquete: NewPaquete): Observable<IPaquete> {
    const copy = this.convertValueFromClient(paquete);
    return this.http.post<RestPaquete>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(paquete: IPaquete): Observable<IPaquete> {
    const copy = this.convertValueFromClient(paquete);
    return this.http
      .put<RestPaquete>(`${this.resourceUrl}/${encodeURIComponent(this.getPaqueteIdentifier(paquete))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(paquete: PartialUpdatePaquete): Observable<IPaquete> {
    const copy = this.convertValueFromClient(paquete);
    return this.http
      .patch<RestPaquete>(`${this.resourceUrl}/${encodeURIComponent(this.getPaqueteIdentifier(paquete))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPaquete> {
    return this.http
      .get<RestPaquete>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPaquete[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPaquete[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPaqueteIdentifier(paquete: Pick<IPaquete, 'id'>): number {
    return paquete.id;
  }

  comparePaquete(o1: Pick<IPaquete, 'id'> | null, o2: Pick<IPaquete, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaqueteIdentifier(o1) === this.getPaqueteIdentifier(o2) : o1 === o2;
  }

  addPaqueteToCollectionIfMissing<Type extends Pick<IPaquete, 'id'>>(
    paqueteCollection: Type[],
    ...paquetesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paquetes: Type[] = paquetesToCheck.filter(paqueteItem => paqueteItem !== null && paqueteItem !== undefined);
    if (paquetes.length > 0) {
      const paqueteCollectionIdentifiers = paqueteCollection.map(paqueteItem => this.getPaqueteIdentifier(paqueteItem));
      const paquetesToAdd = paquetes.filter(paqueteItem => {
        const paqueteIdentifier = this.getPaqueteIdentifier(paqueteItem);
        if (paqueteCollectionIdentifiers.includes(paqueteIdentifier)) {
          return false;
        }
        paqueteCollectionIdentifiers.push(paqueteIdentifier);
        return true;
      });
      return [...paquetesToAdd, ...paqueteCollection];
    }
    return paqueteCollection;
  }

  protected convertValueFromClient<T extends IPaquete | NewPaquete | PartialUpdatePaquete>(paquete: T): RestOf<T> {
    return {
      ...paquete,
      periodo: paquete.periodo?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPaquete): IPaquete {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPaquete[]): IPaquete[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
