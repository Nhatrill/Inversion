import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { InvestmentComponent } from './list/investment.component';
import { InvestmentDetailComponent } from './detail/investment-detail.component';
import { InvestmentUpdateComponent } from './update/investment-update.component';
import { InvestmentDeleteDialogComponent } from './delete/investment-delete-dialog.component';
import { InvestmentRoutingModule } from './route/investment-routing.module';

@NgModule({
  imports: [SharedModule, InvestmentRoutingModule],
  declarations: [InvestmentComponent, InvestmentDetailComponent, InvestmentUpdateComponent, InvestmentDeleteDialogComponent],
  entryComponents: [InvestmentDeleteDialogComponent],
})
export class InvestmentModule {}
