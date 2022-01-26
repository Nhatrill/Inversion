import dayjs from 'dayjs/esm';
import { BankType } from 'app/entities/enumerations/bank-type.model';

export interface IBank {
  id?: number;
  rut?: string | null;
  amount?: string | null;
  bankType?: BankType | null;
  date?: dayjs.Dayjs | null;
}

export class Bank implements IBank {
  constructor(
    public id?: number,
    public rut?: string | null,
    public amount?: string | null,
    public bankType?: BankType | null,
    public date?: dayjs.Dayjs | null
  ) {}
}

export function getBankIdentifier(bank: IBank): number | undefined {
  return bank.id;
}
