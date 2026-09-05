import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IMutual } from '../mutual.model';
import { MutualService } from '../service/mutual.service';

const mutualResolve = (route: ActivatedRouteSnapshot): Observable<null | IMutual> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(MutualService);
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

export default mutualResolve;
