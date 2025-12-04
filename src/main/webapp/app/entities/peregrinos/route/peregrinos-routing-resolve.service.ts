import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPeregrinos } from '../peregrinos.model';
import { PeregrinosService } from '../service/peregrinos.service';

const peregrinosResolve = (route: ActivatedRouteSnapshot): Observable<null | IPeregrinos> => {
  const id = route.params.id;
  if (id) {
    return inject(PeregrinosService)
      .find(id)
      .pipe(
        mergeMap((peregrinos: HttpResponse<IPeregrinos>) => {
          if (peregrinos.body) {
            return of(peregrinos.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default peregrinosResolve;
