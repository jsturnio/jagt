import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IMutual, NewMutual } from '../mutual.model';

export type PartialUpdateMutual = Partial<IMutual> & Pick<IMutual, 'id'>;

@Service()
export class MutualsService {
  readonly mutualsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly mutualsResource = httpResource<IMutual[]>(() => {
    const params = this.mutualsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of mutual that have been fetched. It is updated when the mutualsResource emits a new value.
   * In case of error while fetching the mutuals, the signal is set to an empty array.
   */
  readonly mutuals = computed(() => (this.mutualsResource.hasValue() ? this.mutualsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/mutuals`;
}

@Service()
export class MutualService extends MutualsService {
  protected readonly http = inject(HttpClient);

  create(mutual: NewMutual): Observable<IMutual> {
    return this.http.post<IMutual>(this.resourceUrl, mutual);
  }

  update(mutual: IMutual): Observable<IMutual> {
    return this.http.put<IMutual>(`${this.resourceUrl}/${encodeURIComponent(this.getMutualIdentifier(mutual))}`, mutual);
  }

  partialUpdate(mutual: PartialUpdateMutual): Observable<IMutual> {
    return this.http.patch<IMutual>(`${this.resourceUrl}/${encodeURIComponent(this.getMutualIdentifier(mutual))}`, mutual);
  }

  find(id: number): Observable<IMutual> {
    return this.http.get<IMutual>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IMutual[]>> {
    const options = createRequestOption(req);
    return this.http.get<IMutual[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getMutualIdentifier(mutual: Pick<IMutual, 'id'>): number {
    return mutual.id;
  }

  compareMutual(o1: Pick<IMutual, 'id'> | null, o2: Pick<IMutual, 'id'> | null): boolean {
    return o1 && o2 ? this.getMutualIdentifier(o1) === this.getMutualIdentifier(o2) : o1 === o2;
  }

  addMutualToCollectionIfMissing<Type extends Pick<IMutual, 'id'>>(
    mutualCollection: Type[],
    ...mutualsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mutuals: Type[] = mutualsToCheck.filter(mutualItem => mutualItem !== null && mutualItem !== undefined);
    if (mutuals.length > 0) {
      const mutualCollectionIdentifiers = mutualCollection.map(mutualItem => this.getMutualIdentifier(mutualItem));
      const mutualsToAdd = mutuals.filter(mutualItem => {
        const mutualIdentifier = this.getMutualIdentifier(mutualItem);
        if (mutualCollectionIdentifiers.includes(mutualIdentifier)) {
          return false;
        }
        mutualCollectionIdentifiers.push(mutualIdentifier);
        return true;
      });
      return [...mutualsToAdd, ...mutualCollection];
    }
    return mutualCollection;
  }
}
