import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompetitionProblemFormService } from './competition-problem-form.service';
import { CompetitionProblemService } from '../service/competition-problem.service';
import { ICompetitionProblem } from '../competition-problem.model';
import { IProblem } from 'app/entities/problem/problem.model';
import { ProblemService } from 'app/entities/problem/service/problem.service';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';

import { CompetitionProblemUpdateComponent } from './competition-problem-update.component';

describe('CompetitionProblem Management Update Component', () => {
  let comp: CompetitionProblemUpdateComponent;
  let fixture: ComponentFixture<CompetitionProblemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let competitionProblemFormService: CompetitionProblemFormService;
  let competitionProblemService: CompetitionProblemService;
  let problemService: ProblemService;
  let competitionService: CompetitionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompetitionProblemUpdateComponent],
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
      .overrideTemplate(CompetitionProblemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompetitionProblemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    competitionProblemFormService = TestBed.inject(CompetitionProblemFormService);
    competitionProblemService = TestBed.inject(CompetitionProblemService);
    problemService = TestBed.inject(ProblemService);
    competitionService = TestBed.inject(CompetitionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Problem query and add missing value', () => {
      const competitionProblem: ICompetitionProblem = { id: 456 };
      const problem: IProblem = { id: 68211 };
      competitionProblem.problem = problem;

      const problemCollection: IProblem[] = [{ id: 28152 }];
      jest.spyOn(problemService, 'query').mockReturnValue(of(new HttpResponse({ body: problemCollection })));
      const additionalProblems = [problem];
      const expectedCollection: IProblem[] = [...additionalProblems, ...problemCollection];
      jest.spyOn(problemService, 'addProblemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ competitionProblem });
      comp.ngOnInit();

      expect(problemService.query).toHaveBeenCalled();
      expect(problemService.addProblemToCollectionIfMissing).toHaveBeenCalledWith(
        problemCollection,
        ...additionalProblems.map(expect.objectContaining)
      );
      expect(comp.problemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Competition query and add missing value', () => {
      const competitionProblem: ICompetitionProblem = { id: 456 };
      const competition: ICompetition = { id: 84576 };
      competitionProblem.competition = competition;

      const competitionCollection: ICompetition[] = [{ id: 85667 }];
      jest.spyOn(competitionService, 'query').mockReturnValue(of(new HttpResponse({ body: competitionCollection })));
      const additionalCompetitions = [competition];
      const expectedCollection: ICompetition[] = [...additionalCompetitions, ...competitionCollection];
      jest.spyOn(competitionService, 'addCompetitionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ competitionProblem });
      comp.ngOnInit();

      expect(competitionService.query).toHaveBeenCalled();
      expect(competitionService.addCompetitionToCollectionIfMissing).toHaveBeenCalledWith(
        competitionCollection,
        ...additionalCompetitions.map(expect.objectContaining)
      );
      expect(comp.competitionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const competitionProblem: ICompetitionProblem = { id: 456 };
      const problem: IProblem = { id: 16364 };
      competitionProblem.problem = problem;
      const competition: ICompetition = { id: 40917 };
      competitionProblem.competition = competition;

      activatedRoute.data = of({ competitionProblem });
      comp.ngOnInit();

      expect(comp.problemsSharedCollection).toContain(problem);
      expect(comp.competitionsSharedCollection).toContain(competition);
      expect(comp.competitionProblem).toEqual(competitionProblem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompetitionProblem>>();
      const competitionProblem = { id: 123 };
      jest.spyOn(competitionProblemFormService, 'getCompetitionProblem').mockReturnValue(competitionProblem);
      jest.spyOn(competitionProblemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competitionProblem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: competitionProblem }));
      saveSubject.complete();

      // THEN
      expect(competitionProblemFormService.getCompetitionProblem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(competitionProblemService.update).toHaveBeenCalledWith(expect.objectContaining(competitionProblem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompetitionProblem>>();
      const competitionProblem = { id: 123 };
      jest.spyOn(competitionProblemFormService, 'getCompetitionProblem').mockReturnValue({ id: null });
      jest.spyOn(competitionProblemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competitionProblem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: competitionProblem }));
      saveSubject.complete();

      // THEN
      expect(competitionProblemFormService.getCompetitionProblem).toHaveBeenCalled();
      expect(competitionProblemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompetitionProblem>>();
      const competitionProblem = { id: 123 };
      jest.spyOn(competitionProblemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ competitionProblem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(competitionProblemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProblem', () => {
      it('Should forward to problemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(problemService, 'compareProblem');
        comp.compareProblem(entity, entity2);
        expect(problemService.compareProblem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCompetition', () => {
      it('Should forward to competitionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(competitionService, 'compareCompetition');
        comp.compareCompetition(entity, entity2);
        expect(competitionService.compareCompetition).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
