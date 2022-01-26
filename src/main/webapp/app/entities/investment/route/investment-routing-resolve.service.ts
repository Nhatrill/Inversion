import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvestment, Investment } from '../investment.model';
import { InvestmentService } from '../service/investment.service';

@Injectable({ providedIn: 'root' })
export class InvestmentRoutingResolveService implements Resolve<IInvestment> {
  constructor(protected service: InvestmentService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInvestment> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((investment: HttpResponse<Investment>) => {
          if (investment.body) {
            return of(investment.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Investment());
  }
}
