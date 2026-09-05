import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IBioquimico, NewBioquimico } from '../bioquimico.model';

export type PartialUpdateBioquimico = Partial<IBioquimico> & Pick<IBioquimico, 'id'>;

type RestOf<T extends IBioquimico | NewBioquimico> = Omit<T, 'fechaIngreso'> & {
  fechaIngreso?: string | null;
};

export type RestBioquimico = RestOf<IBioquimico>;

export type NewRestBioquimico = RestOf<NewBioquimico>;

export type PartialUpdateRestBioquimico = RestOf<PartialUpdateBioquimico>;

@Service()
export class BioquimicosService {
  readonly bioquimicosParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly bioquimicosResource = httpResource<RestBioquimico[]>(() => {
    const params = this.bioquimicosParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of bioquimico that have been fetched. It is updated when the bioquimicosResource emits a new value.
   * In case of error while fetching the bioquimicos, the signal is set to an empty array.
   */
  readonly bioquimicos = computed(() =>
    (this.bioquimicosResource.hasValue() ? this.bioquimicosResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/bioquimicos`;

  protected convertValueFromServer(restBioquimico: RestBioquimico): IBioquimico {
    return {
      ...restBioquimico,
      fechaIngreso: restBioquimico.fechaIngreso ? dayjs(restBioquimico.fechaIngreso) : undefined,
    };
  }
}

@Service()
export class BioquimicoService extends BioquimicosService {
  protected readonly http = inject(HttpClient);

  create(bioquimico: NewBioquimico): Observable<IBioquimico> {
    const copy = this.convertValueFromClient(bioquimico);
    return this.http.post<RestBioquimico>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bioquimico: IBioquimico): Observable<IBioquimico> {
    const copy = this.convertValueFromClient(bioquimico);
    return this.http
      .put<RestBioquimico>(`${this.resourceUrl}/${encodeURIComponent(this.getBioquimicoIdentifier(bioquimico))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bioquimico: PartialUpdateBioquimico): Observable<IBioquimico> {
    const copy = this.convertValueFromClient(bioquimico);
    return this.http
      .patch<RestBioquimico>(`${this.resourceUrl}/${encodeURIComponent(this.getBioquimicoIdentifier(bioquimico))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IBioquimico> {
    return this.http
      .get<RestBioquimico>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IBioquimico[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBioquimico[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getBioquimicoIdentifier(bioquimico: Pick<IBioquimico, 'id'>): number {
    return bioquimico.id;
  }

  compareBioquimico(o1: Pick<IBioquimico, 'id'> | null, o2: Pick<IBioquimico, 'id'> | null): boolean {
    return o1 && o2 ? this.getBioquimicoIdentifier(o1) === this.getBioquimicoIdentifier(o2) : o1 === o2;
  }

  addBioquimicoToCollectionIfMissing<Type extends Pick<IBioquimico, 'id'>>(
    bioquimicoCollection: Type[],
    ...bioquimicosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bioquimicos: Type[] = bioquimicosToCheck.filter(bioquimicoItem => bioquimicoItem !== null && bioquimicoItem !== undefined);
    if (bioquimicos.length > 0) {
      const bioquimicoCollectionIdentifiers = bioquimicoCollection.map(bioquimicoItem => this.getBioquimicoIdentifier(bioquimicoItem));
      const bioquimicosToAdd = bioquimicos.filter(bioquimicoItem => {
        const bioquimicoIdentifier = this.getBioquimicoIdentifier(bioquimicoItem);
        if (bioquimicoCollectionIdentifiers.includes(bioquimicoIdentifier)) {
          return false;
        }
        bioquimicoCollectionIdentifiers.push(bioquimicoIdentifier);
        return true;
      });
      return [...bioquimicosToAdd, ...bioquimicoCollection];
    }
    return bioquimicoCollection;
  }

  protected convertValueFromClient<T extends IBioquimico | NewBioquimico | PartialUpdateBioquimico>(bioquimico: T): RestOf<T> {
    return {
      ...bioquimico,
      fechaIngreso: bioquimico.fechaIngreso?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestBioquimico): IBioquimico {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestBioquimico[]): IBioquimico[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
