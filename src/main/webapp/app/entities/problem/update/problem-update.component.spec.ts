import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProblemFormService } from './problem-form.service';
import { ProblemService } from '../service/problem.service';
import { IProblem } from '../problem.model';
import { ITagCollection } from 'app/entities/tag-collection/tag-collection.model';
import { TagCollectionService } from 'app/entities/tag-collection/service/tag-collection.service';

import { ProblemUpdateComponent } from './problem-update.component';

describe('Problem Management Update Component', () => {
  let comp: ProblemUpdateComponent;
  let fixture: ComponentFixture<ProblemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let problemFormService: ProblemFormService;
  let problemService: ProblemService;
  let tagCollectionService: TagCollectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProblemUpdateComponent],
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
      .overrideTemplate(ProblemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProblemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    problemFormService = TestBed.inject(ProblemFormService);
    problemService = TestBed.inject(ProblemService);
    tagCollectionService = TestBed.inject(TagCollectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TagCollection query and add missing value', () => {
      const problem: IProblem = { id: 456 };
      const tags: ITagCollection = { id: 33427 };
      problem.tags = tags;

      const tagCollectionCollection: ITagCollection[] = [{ id: 35246 }];
      jest.spyOn(tagCollectionService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollectionCollection })));
      const additionalTagCollections = [tags];
      const expectedCollection: ITagCollection[] = [...additionalTagCollections, ...tagCollectionCollection];
      jest.spyOn(tagCollectionService, 'addTagCollectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ problem });
      comp.ngOnInit();

      expect(tagCollectionService.query).toHaveBeenCalled();
      expect(tagCollectionService.addTagCollectionToCollectionIfMissing).toHaveBeenCalledWith(
        tagCollectionCollection,
        ...additionalTagCollections.map(expect.objectContaining)
      );
      expect(comp.tagCollectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const problem: IProblem = { id: 456 };
      const tags: ITagCollection = { id: 12287 };
      problem.tags = tags;

      activatedRoute.data = of({ problem });
      comp.ngOnInit();

      expect(comp.tagCollectionsSharedCollection).toContain(tags);
      expect(comp.problem).toEqual(problem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProblem>>();
      const problem = { id: 123 };
      jest.spyOn(problemFormService, 'getProblem').mockReturnValue(problem);
      jest.spyOn(problemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ problem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: problem }));
      saveSubject.complete();

      // THEN
      expect(problemFormService.getProblem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(problemService.update).toHaveBeenCalledWith(expect.objectContaining(problem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProblem>>();
      const problem = { id: 123 };
      jest.spyOn(problemFormService, 'getProblem').mockReturnValue({ id: null });
      jest.spyOn(problemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ problem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: problem }));
      saveSubject.complete();

      // THEN
      expect(problemFormService.getProblem).toHaveBeenCalled();
      expect(problemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProblem>>();
      const problem = { id: 123 };
      jest.spyOn(problemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ problem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(problemService.update).toHaveBeenCalled();
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
  });
});
