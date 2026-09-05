import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPlanMutual } from '../plan-mutual.model';
import { PlanMutualService } from '../service/plan-mutual.service';

const planMutualResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlanMutual> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PlanMutualService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default planMutualResolve;
