import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InvestmentDetailComponent } from './investment-detail.component';

describe('Investment Management Detail Component', () => {
  let comp: InvestmentDetailComponent;
  let fixture: ComponentFixture<InvestmentDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InvestmentDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ investment: { id: '9fec3727-3421-4967-b213-ba36557ca194' } }) },
        },
      ],
    })
      .overrideTemplate(InvestmentDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InvestmentDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load investment on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.investment).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
    });
  });
});
