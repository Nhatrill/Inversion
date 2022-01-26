import dayjs from 'dayjs/esm';

export interface IInvestment {
  id?: string;
  rut?: string | null;
  startDate?: dayjs.Dayjs | null;
  updateDate?: dayjs.Dayjs | null;
  totalAmount?: string | null;
}

export class Investment implements IInvestment {
  constructor(
    public id?: string,
    public rut?: string | null,
    public startDate?: dayjs.Dayjs | null,
    public updateDate?: dayjs.Dayjs | null,
    public totalAmount?: string | null
  ) {}
}

export function getInvestmentIdentifier(investment: IInvestment): string | undefined {
  return investment.id;
}
