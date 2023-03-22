import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompetitionProblemDetailComponent } from './competition-problem-detail.component';

describe('CompetitionProblem Management Detail Component', () => {
  let comp: CompetitionProblemDetailComponent;
  let fixture: ComponentFixture<CompetitionProblemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CompetitionProblemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ competitionProblem: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CompetitionProblemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CompetitionProblemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load competitionProblem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.competitionProblem).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
