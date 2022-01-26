import dayjs from 'dayjs/esm';
import { ProductType } from 'app/entities/enumerations/product-type.model';

export interface IProduct {
  id?: number;
  rut?: string | null;
  initialInvestment?: string | null;
  totalAmount?: string | null;
  productType?: ProductType | null;
  date?: dayjs.Dayjs | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public rut?: string | null,
    public initialInvestment?: string | null,
    public totalAmount?: string | null,
    public productType?: ProductType | null,
    public date?: dayjs.Dayjs | null
  ) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
