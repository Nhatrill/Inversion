import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvestmentService } from '../service/investment.service';
import { IInvestment, Investment } from '../investment.model';

import { InvestmentUpdateComponent } from './investment-update.component';

describe('Investment Management Update Component', () => {
  let comp: InvestmentUpdateComponent;
  let fixture: ComponentFixture<InvestmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let investmentService: InvestmentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InvestmentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InvestmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvestmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    investmentService = TestBed.inject(InvestmentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const investment: IInvestment = { id: '1361f429-3817-4123-8ee3-fdf8943310b2' };

      activatedRoute.data = of({ investment });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(investment));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Investment>>();
      const investment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(investmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: investment }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(investmentService.update).toHaveBeenCalledWith(investment);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Investment>>();
      const investment = new Investment();
      jest.spyOn(investmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: investment }));
      saveSubject.complete();

      // THEN
      expect(investmentService.create).toHaveBeenCalledWith(investment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Investment>>();
      const investment = { id: '9fec3727-3421-4967-b213-ba36557ca194' };
      jest.spyOn(investmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ investment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(investmentService.update).toHaveBeenCalledWith(investment);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
