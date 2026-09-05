import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPlanMutual, NewPlanMutual } from '../plan-mutual.model';

export type PartialUpdatePlanMutual = Partial<IPlanMutual> & Pick<IPlanMutual, 'id'>;

@Service()
export class PlanMutualsService {
  readonly planMutualsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly planMutualsResource = httpResource<IPlanMutual[]>(() => {
    const params = this.planMutualsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of planMutual that have been fetched. It is updated when the planMutualsResource emits a new value.
   * In case of error while fetching the planMutuals, the signal is set to an empty array.
   */
  readonly planMutuals = computed(() => (this.planMutualsResource.hasValue() ? this.planMutualsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/plan-mutuals`;
}

@Service()
export class PlanMutualService extends PlanMutualsService {
  protected readonly http = inject(HttpClient);

  create(planMutual: NewPlanMutual): Observable<IPlanMutual> {
    return this.http.post<IPlanMutual>(this.resourceUrl, planMutual);
  }

  update(planMutual: IPlanMutual): Observable<IPlanMutual> {
    return this.http.put<IPlanMutual>(`${this.resourceUrl}/${encodeURIComponent(this.getPlanMutualIdentifier(planMutual))}`, planMutual);
  }

  partialUpdate(planMutual: PartialUpdatePlanMutual): Observable<IPlanMutual> {
    return this.http.patch<IPlanMutual>(`${this.resourceUrl}/${encodeURIComponent(this.getPlanMutualIdentifier(planMutual))}`, planMutual);
  }

  find(id: number): Observable<IPlanMutual> {
    return this.http.get<IPlanMutual>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IPlanMutual[]>> {
    const options = createRequestOption(req);
    return this.http.get<IPlanMutual[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPlanMutualIdentifier(planMutual: Pick<IPlanMutual, 'id'>): number {
    return planMutual.id;
  }

  comparePlanMutual(o1: Pick<IPlanMutual, 'id'> | null, o2: Pick<IPlanMutual, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlanMutualIdentifier(o1) === this.getPlanMutualIdentifier(o2) : o1 === o2;
  }

  addPlanMutualToCollectionIfMissing<Type extends Pick<IPlanMutual, 'id'>>(
    planMutualCollection: Type[],
    ...planMutualsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const planMutuals: Type[] = planMutualsToCheck.filter(planMutualItem => planMutualItem !== null && planMutualItem !== undefined);
    if (planMutuals.length > 0) {
      const planMutualCollectionIdentifiers = planMutualCollection.map(planMutualItem => this.getPlanMutualIdentifier(planMutualItem));
      const planMutualsToAdd = planMutuals.filter(planMutualItem => {
        const planMutualIdentifier = this.getPlanMutualIdentifier(planMutualItem);
        if (planMutualCollectionIdentifiers.includes(planMutualIdentifier)) {
          return false;
        }
        planMutualCollectionIdentifiers.push(planMutualIdentifier);
        return true;
      });
      return [...planMutualsToAdd, ...planMutualCollection];
    }
    return planMutualCollection;
  }
}
