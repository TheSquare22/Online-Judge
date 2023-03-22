import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SubmissionFormService } from './submission-form.service';
import { SubmissionService } from '../service/submission.service';
import { ISubmission } from '../submission.model';
import { ITagCollection } from 'app/entities/tag-collection/tag-collection.model';
import { TagCollectionService } from 'app/entities/tag-collection/service/tag-collection.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICompetitionProblem } from 'app/entities/competition-problem/competition-problem.model';
import { CompetitionProblemService } from 'app/entities/competition-problem/service/competition-problem.service';

import { SubmissionUpdateComponent } from './submission-update.component';

describe('Submission Management Update Component', () => {
  let comp: SubmissionUpdateComponent;
  let fixture: ComponentFixture<SubmissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let submissionFormService: SubmissionFormService;
  let submissionService: SubmissionService;
  let tagCollectionService: TagCollectionService;
  let userService: UserService;
  let competitionProblemService: CompetitionProblemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SubmissionUpdateComponent],
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
      .overrideTemplate(SubmissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubmissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    submissionFormService = TestBed.inject(SubmissionFormService);
    submissionService = TestBed.inject(SubmissionService);
    tagCollectionService = TestBed.inject(TagCollectionService);
    userService = TestBed.inject(UserService);
    competitionProblemService = TestBed.inject(CompetitionProblemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TagCollection query and add missing value', () => {
      const submission: ISubmission = { id: 456 };
      const tags: ITagCollection = { id: 15544 };
      submission.tags = tags;

      const tagCollectionCollection: ITagCollection[] = [{ id: 1416 }];
      jest.spyOn(tagCollectionService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollectionCollection })));
      const additionalTagCollections = [tags];
      const expectedCollection: ITagCollection[] = [...additionalTagCollections, ...tagCollectionCollection];
      jest.spyOn(tagCollectionService, 'addTagCollectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(tagCollectionService.query).toHaveBeenCalled();
      expect(tagCollectionService.addTagCollectionToCollectionIfMissing).toHaveBeenCalledWith(
        tagCollectionCollection,
        ...additionalTagCollections.map(expect.objectContaining)
      );
      expect(comp.tagCollectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const submission: ISubmission = { id: 456 };
      const user: IUser = { id: 25956 };
      submission.user = user;

      const userCollection: IUser[] = [{ id: 86867 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining)
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CompetitionProblem query and add missing value', () => {
      const submission: ISubmission = { id: 456 };
      const competitionProblem: ICompetitionProblem = { id: 69739 };
      submission.competitionProblem = competitionProblem;

      const competitionProblemCollection: ICompetitionProblem[] = [{ id: 84857 }];
      jest.spyOn(competitionProblemService, 'query').mockReturnValue(of(new HttpResponse({ body: competitionProblemCollection })));
      const additionalCompetitionProblems = [competitionProblem];
      const expectedCollection: ICompetitionProblem[] = [...additionalCompetitionProblems, ...competitionProblemCollection];
      jest.spyOn(competitionProblemService, 'addCompetitionProblemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(competitionProblemService.query).toHaveBeenCalled();
      expect(competitionProblemService.addCompetitionProblemToCollectionIfMissing).toHaveBeenCalledWith(
        competitionProblemCollection,
        ...additionalCompetitionProblems.map(expect.objectContaining)
      );
      expect(comp.competitionProblemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const submission: ISubmission = { id: 456 };
      const tags: ITagCollection = { id: 18387 };
      submission.tags = tags;
      const user: IUser = { id: 69011 };
      submission.user = user;
      const competitionProblem: ICompetitionProblem = { id: 22573 };
      submission.competitionProblem = competitionProblem;

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(comp.tagCollectionsSharedCollection).toContain(tags);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.competitionProblemsSharedCollection).toContain(competitionProblem);
      expect(comp.submission).toEqual(submission);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 123 };
      jest.spyOn(submissionFormService, 'getSubmission').mockReturnValue(submission);
      jest.spyOn(submissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: submission }));
      saveSubject.complete();

      // THEN
      expect(submissionFormService.getSubmission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(submissionService.update).toHaveBeenCalledWith(expect.objectContaining(submission));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 123 };
      jest.spyOn(submissionFormService, 'getSubmission').mockReturnValue({ id: null });
      jest.spyOn(submissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: submission }));
      saveSubject.complete();

      // THEN
      expect(submissionFormService.getSubmission).toHaveBeenCalled();
      expect(submissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 123 };
      jest.spyOn(submissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(submissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTagCollection', () => {
      it('Should forward to tagCollectionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tagCollectionService, 'compareTagCollection');
        comp.compareTagCollection(entity, entity2);
        expect(tagCollectionService.compareTagCollection).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCompetitionProblem', () => {
      it('Should forward to competitionProblemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(competitionProblemService, 'compareCompetitionProblem');
        comp.compareCompetitionProblem(entity, entity2);
        expect(competitionProblemService.compareCompetitionProblem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
