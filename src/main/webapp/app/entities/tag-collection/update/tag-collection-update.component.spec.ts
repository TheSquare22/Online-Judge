import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TagCollectionFormService } from './tag-collection-form.service';
import { TagCollectionService } from '../service/tag-collection.service';
import { ITagCollection } from '../tag-collection.model';

import { TagCollectionUpdateComponent } from './tag-collection-update.component';

describe('TagCollection Management Update Component', () => {
  let comp: TagCollectionUpdateComponent;
  let fixture: ComponentFixture<TagCollectionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tagCollectionFormService: TagCollectionFormService;
  let tagCollectionService: TagCollectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TagCollectionUpdateComponent],
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
      .overrideTemplate(TagCollectionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TagCollectionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tagCollectionFormService = TestBed.inject(TagCollectionFormService);
    tagCollectionService = TestBed.inject(TagCollectionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const tagCollection: ITagCollection = { id: 456 };

      activatedRoute.data = of({ tagCollection });
      comp.ngOnInit();

      expect(comp.tagCollection).toEqual(tagCollection);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagCollection>>();
      const tagCollection = { id: 123 };
      jest.spyOn(tagCollectionFormService, 'getTagCollection').mockReturnValue(tagCollection);
      jest.spyOn(tagCollectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagCollection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tagCollection }));
      saveSubject.complete();

      // THEN
      expect(tagCollectionFormService.getTagCollection).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tagCollectionService.update).toHaveBeenCalledWith(expect.objectContaining(tagCollection));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagCollection>>();
      const tagCollection = { id: 123 };
      jest.spyOn(tagCollectionFormService, 'getTagCollection').mockReturnValue({ id: null });
      jest.spyOn(tagCollectionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagCollection: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tagCollection }));
      saveSubject.complete();

      // THEN
      expect(tagCollectionFormService.getTagCollection).toHaveBeenCalled();
      expect(tagCollectionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagCollection>>();
      const tagCollection = { id: 123 };
      jest.spyOn(tagCollectionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagCollection });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tagCollectionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
