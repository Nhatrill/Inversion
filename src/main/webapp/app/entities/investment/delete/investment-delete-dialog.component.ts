import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IInvestment } from '../investment.model';
import { InvestmentService } from '../service/investment.service';

@Component({
  templateUrl: './investment-delete-dialog.component.html',
})
export class InvestmentDeleteDialogComponent {
  investment?: IInvestment;

  constructor(protected investmentService: InvestmentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.investmentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
