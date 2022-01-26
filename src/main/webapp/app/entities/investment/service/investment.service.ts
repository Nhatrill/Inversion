import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvestment, getInvestmentIdentifier } from '../investment.model';

export type EntityResponseType = HttpResponse<IInvestment>;
export type EntityArrayResponseType = HttpResponse<IInvestment[]>;

@Injectable({ providedIn: 'root' })
export class InvestmentService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/investments');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(investment: IInvestment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(investment);
    return this.http
      .post<IInvestment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(investment: IInvestment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(investment);
    return this.http
      .put<IInvestment>(`${this.resourceUrl}/${getInvestmentIdentifier(investment) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(investment: IInvestment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(investment);
    return this.http
      .patch<IInvestment>(`${this.resourceUrl}/${getInvestmentIdentifier(investment) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IInvestment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInvestment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addInvestmentToCollectionIfMissing(
    investmentCollection: IInvestment[],
    ...investmentsToCheck: (IInvestment | null | undefined)[]
  ): IInvestment[] {
    const investments: IInvestment[] = investmentsToCheck.filter(isPresent);
    if (investments.length > 0) {
      const investmentCollectionIdentifiers = investmentCollection.map(investmentItem => getInvestmentIdentifier(investmentItem)!);
      const investmentsToAdd = investments.filter(investmentItem => {
        const investmentIdentifier = getInvestmentIdentifier(investmentItem);
        if (investmentIdentifier == null || investmentCollectionIdentifiers.includes(investmentIdentifier)) {
          return false;
        }
        investmentCollectionIdentifiers.push(investmentIdentifier);
        return true;
      });
      return [...investmentsToAdd, ...investmentCollection];
    }
    return investmentCollection;
  }

  protected convertDateFromClient(investment: IInvestment): IInvestment {
    return Object.assign({}, investment, {
      startDate: investment.startDate?.isValid() ? investment.startDate.toJSON() : undefined,
      updateDate: investment.updateDate?.isValid() ? investment.updateDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.updateDate = res.body.updateDate ? dayjs(res.body.updateDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((investment: IInvestment) => {
        investment.startDate = investment.startDate ? dayjs(investment.startDate) : undefined;
        investment.updateDate = investment.updateDate ? dayjs(investment.updateDate) : undefined;
      });
    }
    return res;
  }
}
