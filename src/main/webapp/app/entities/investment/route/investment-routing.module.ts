import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvestmentComponent } from '../list/investment.component';
import { InvestmentDetailComponent } from '../detail/investment-detail.component';
import { InvestmentUpdateComponent } from '../update/investment-update.component';
import { InvestmentRoutingResolveService } from './investment-routing-resolve.service';

const investmentRoute: Routes = [
  {
    path: '',
    component: InvestmentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvestmentDetailComponent,
    resolve: {
      investment: InvestmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvestmentUpdateComponent,
    resolve: {
      investment: InvestmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvestmentUpdateComponent,
    resolve: {
      investment: InvestmentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(investmentRoute)],
  exports: [RouterModule],
})
export class InvestmentRoutingModule {}
