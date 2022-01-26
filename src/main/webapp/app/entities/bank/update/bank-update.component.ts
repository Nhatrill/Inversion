import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBank, Bank } from '../bank.model';
import { BankService } from '../service/bank.service';
import { BankType } from 'app/entities/enumerations/bank-type.model';

@Component({
  selector: 'jhi-bank-update',
  templateUrl: './bank-update.component.html',
})
export class BankUpdateComponent implements OnInit {
  isSaving = false;
  bankTypeValues = Object.keys(BankType);

  editForm = this.fb.group({
    id: [],
    rut: [],
    amount: [],
    bankType: [],
    date: [],
  });

  constructor(protected bankService: BankService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bank }) => {
      if (bank.id === undefined) {
        const today = dayjs().startOf('day');
        bank.date = today;
      }

      this.updateForm(bank);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bank = this.createFromForm();
    if (bank.id !== undefined) {
      this.subscribeToSaveResponse(this.bankService.update(bank));
    } else {
      this.subscribeToSaveResponse(this.bankService.create(bank));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBank>>): void {
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

  protected updateForm(bank: IBank): void {
    this.editForm.patchValue({
      id: bank.id,
      rut: bank.rut,
      amount: bank.amount,
      bankType: bank.bankType,
      date: bank.date ? bank.date.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IBank {
    return {
      ...new Bank(),
      id: this.editForm.get(['id'])!.value,
      rut: this.editForm.get(['rut'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      bankType: this.editForm.get(['bankType'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
