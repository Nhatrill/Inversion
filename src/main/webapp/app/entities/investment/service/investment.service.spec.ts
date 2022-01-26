import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvestment, Investment } from '../investment.model';

import { InvestmentService } from './investment.service';

describe('Investment Service', () => {
  let service: InvestmentService;
  let httpMock: HttpTestingController;
  let elemDefault: IInvestment;
  let expectedResult: IInvestment | IInvestment[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InvestmentService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 'AAAAAAA',
      rut: 'AAAAAAA',
      startDate: currentDate,
      updateDate: currentDate,
      totalAmount: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          startDate: currentDate.format(DATE_TIME_FORMAT),
          updateDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Investment', () => {
      const returnedFromService = Object.assign(
        {
          id: 'ID',
          startDate: currentDate.format(DATE_TIME_FORMAT),
          updateDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          updateDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Investment()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Investment', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          rut: 'BBBBBB',
          startDate: currentDate.format(DATE_TIME_FORMAT),
          updateDate: currentDate.format(DATE_TIME_FORMAT),
          totalAmount: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          updateDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Investment', () => {
      const patchObject = Object.assign(
        {
          totalAmount: 'BBBBBB',
        },
        new Investment()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          startDate: currentDate,
          updateDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Investment', () => {
      const returnedFromService = Object.assign(
        {
          id: 'BBBBBB',
          rut: 'BBBBBB',
          startDate: currentDate.format(DATE_TIME_FORMAT),
          updateDate: currentDate.format(DATE_TIME_FORMAT),
          totalAmount: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          updateDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Investment', () => {
      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addInvestmentToCollectionIfMissing', () => {
      it('should add a Investment to an empty array', () => {
        const investment: IInvestment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        expectedResult = service.addInvestmentToCollectionIfMissing([], investment);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(investment);
      });

      it('should not add a Investment to an array that contains it', () => {
        const investment: IInvestment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const investmentCollection: IInvestment[] = [
          {
            ...investment,
          },
          { id: '1361f429-3817-4123-8ee3-fdf8943310b2' },
        ];
        expectedResult = service.addInvestmentToCollectionIfMissing(investmentCollection, investment);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Investment to an array that doesn't contain it", () => {
        const investment: IInvestment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const investmentCollection: IInvestment[] = [{ id: '1361f429-3817-4123-8ee3-fdf8943310b2' }];
        expectedResult = service.addInvestmentToCollectionIfMissing(investmentCollection, investment);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(investment);
      });

      it('should add only unique Investment to an array', () => {
        const investmentArray: IInvestment[] = [
          { id: '9fec3727-3421-4967-b213-ba36557ca194' },
          { id: '1361f429-3817-4123-8ee3-fdf8943310b2' },
          { id: '13ee75f8-559a-4fd0-ad48-6f8b33893849' },
        ];
        const investmentCollection: IInvestment[] = [{ id: '9fec3727-3421-4967-b213-ba36557ca194' }];
        expectedResult = service.addInvestmentToCollectionIfMissing(investmentCollection, ...investmentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const investment: IInvestment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        const investment2: IInvestment = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };
        expectedResult = service.addInvestmentToCollectionIfMissing([], investment, investment2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(investment);
        expect(expectedResult).toContain(investment2);
      });

      it('should accept null and undefined values', () => {
        const investment: IInvestment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
        expectedResult = service.addInvestmentToCollectionIfMissing([], null, investment, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(investment);
      });

      it('should return initial array if no Investment is added', () => {
        const investmentCollection: IInvestment[] = [{ id: '9fec3727-3421-4967-b213-ba36557ca194' }];
        expectedResult = service.addInvestmentToCollectionIfMissing(investmentCollection, undefined, null);
        expect(expectedResult).toEqual(investmentCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
