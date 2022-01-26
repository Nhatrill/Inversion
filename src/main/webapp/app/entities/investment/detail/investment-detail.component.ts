import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInvestment } from '../investment.model';

@Component({
  selector: 'jhi-investment-detail',
  templateUrl: './investment-detail.component.html',
})
export class InvestmentDetailComponent implements OnInit {
  investment: IInvestment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ investment }) => {
      this.investment = investment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
