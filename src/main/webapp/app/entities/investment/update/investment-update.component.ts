import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInvestment, Investment } from '../investment.model';
import { InvestmentService } from '../service/investment.service';

@Component({
  selector: 'jhi-investment-update',
  templateUrl: './investment-update.component.html',
})
export class InvestmentUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    rut: [],
    startDate: [],
    updateDate: [],
    totalAmount: [],
  });

  constructor(protected investmentService: InvestmentService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ investment }) => {
      if (investment.id === undefined) {
        const today = dayjs().startOf('day');
        investment.startDate = today;
        investment.updateDate = today;
      }

      this.updateForm(investment);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const investment = this.createFromForm();
    if (investment.id !== undefined) {
      this.subscribeToSaveResponse(this.investmentService.update(investment));
    } else {
      this.subscribeToSaveResponse(this.investmentService.create(investment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvestment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(investment: IInvestment): void {
    this.editForm.patchValue({
      id: investment.id,
      rut: investment.rut,
      startDate: investment.startDate ? investment.startDate.format(DATE_TIME_FORMAT) : null,
      updateDate: investment.updateDate ? investment.updateDate.format(DATE_TIME_FORMAT) : null,
      totalAmount: investment.totalAmount,
    });
  }

  protected createFromForm(): IInvestment {
    return {
      ...new Investment(),
      id: this.editForm.get(['id'])!.value,
      rut: this.editForm.get(['rut'])!.value,
      startDate: this.editForm.get(['startDate'])!.value ? dayjs(this.editForm.get(['startDate'])!.value, DATE_TIME_FORMAT) : undefined,
      updateDate: this.editForm.get(['updateDate'])!.value ? dayjs(this.editForm.get(['updateDate'])!.value, DATE_TIME_FORMAT) : undefined,
      totalAmount: this.editForm.get(['totalAmount'])!.value,
    };
  }
}
