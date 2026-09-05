import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { INomenclador } from '../nomenclador.model';
import { NomencladorService } from '../service/nomenclador.service';

const nomencladorResolve = (route: ActivatedRouteSnapshot): Observable<null | INomenclador> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(NomencladorService);
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

export default nomencladorResolve;
