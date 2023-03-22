import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TagCollectionTagFormService } from './tag-collection-tag-form.service';
import { TagCollectionTagService } from '../service/tag-collection-tag.service';
import { ITagCollectionTag } from '../tag-collection-tag.model';
import { ITagCollection } from 'app/entities/tag-collection/tag-collection.model';
import { TagCollectionService } from 'app/entities/tag-collection/service/tag-collection.service';
import { ITag } from 'app/entities/tag/tag.model';
import { TagService } from 'app/entities/tag/service/tag.service';

import { TagCollectionTagUpdateComponent } from './tag-collection-tag-update.component';

describe('TagCollectionTag Management Update Component', () => {
  let comp: TagCollectionTagUpdateComponent;
  let fixture: ComponentFixture<TagCollectionTagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tagCollectionTagFormService: TagCollectionTagFormService;
  let tagCollectionTagService: TagCollectionTagService;
  let tagCollectionService: TagCollectionService;
  let tagService: TagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TagCollectionTagUpdateComponent],
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
      .overrideTemplate(TagCollectionTagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TagCollectionTagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tagCollectionTagFormService = TestBed.inject(TagCollectionTagFormService);
    tagCollectionTagService = TestBed.inject(TagCollectionTagService);
    tagCollectionService = TestBed.inject(TagCollectionService);
    tagService = TestBed.inject(TagService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call TagCollection query and add missing value', () => {
      const tagCollectionTag: ITagCollectionTag = { id: 456 };
      const collection: ITagCollection = { id: 89448 };
      tagCollectionTag.collection = collection;

      const tagCollectionCollection: ITagCollection[] = [{ id: 98664 }];
      jest.spyOn(tagCollectionService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollectionCollection })));
      const additionalTagCollections = [collection];
      const expectedCollection: ITagCollection[] = [...additionalTagCollections, ...tagCollectionCollection];
      jest.spyOn(tagCollectionService, 'addTagCollectionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tagCollectionTag });
      comp.ngOnInit();

      expect(tagCollectionService.query).toHaveBeenCalled();
      expect(tagCollectionService.addTagCollectionToCollectionIfMissing).toHaveBeenCalledWith(
        tagCollectionCollection,
        ...additionalTagCollections.map(expect.objectContaining)
      );
      expect(comp.tagCollectionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Tag query and add missing value', () => {
      const tagCollectionTag: ITagCollectionTag = { id: 456 };
      const tag: ITag = { id: 67715 };
      tagCollectionTag.tag = tag;

      const tagCollection: ITag[] = [{ id: 20156 }];
      jest.spyOn(tagService, 'query').mockReturnValue(of(new HttpResponse({ body: tagCollection })));
      const additionalTags = [tag];
      const expectedCollection: ITag[] = [...additionalTags, ...tagCollection];
      jest.spyOn(tagService, 'addTagToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tagCollectionTag });
      comp.ngOnInit();

      expect(tagService.query).toHaveBeenCalled();
      expect(tagService.addTagToCollectionIfMissing).toHaveBeenCalledWith(tagCollection, ...additionalTags.map(expect.objectContaining));
      expect(comp.tagsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tagCollectionTag: ITagCollectionTag = { id: 456 };
      const collection: ITagCollection = { id: 63556 };
      tagCollectionTag.collection = collection;
      const tag: ITag = { id: 16176 };
      tagCollectionTag.tag = tag;

      activatedRoute.data = of({ tagCollectionTag });
      comp.ngOnInit();

      expect(comp.tagCollectionsSharedCollection).toContain(collection);
      expect(comp.tagsSharedCollection).toContain(tag);
      expect(comp.tagCollectionTag).toEqual(tagCollectionTag);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagCollectionTag>>();
      const tagCollectionTag = { id: 123 };
      jest.spyOn(tagCollectionTagFormService, 'getTagCollectionTag').mockReturnValue(tagCollectionTag);
      jest.spyOn(tagCollectionTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagCollectionTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tagCollectionTag }));
      saveSubject.complete();

      // THEN
      expect(tagCollectionTagFormService.getTagCollectionTag).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tagCollectionTagService.update).toHaveBeenCalledWith(expect.objectContaining(tagCollectionTag));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagCollectionTag>>();
      const tagCollectionTag = { id: 123 };
      jest.spyOn(tagCollectionTagFormService, 'getTagCollectionTag').mockReturnValue({ id: null });
      jest.spyOn(tagCollectionTagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagCollectionTag: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tagCollectionTag }));
      saveSubject.complete();

      // THEN
      expect(tagCollectionTagFormService.getTagCollectionTag).toHaveBeenCalled();
      expect(tagCollectionTagService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITagCollectionTag>>();
      const tagCollectionTag = { id: 123 };
      jest.spyOn(tagCollectionTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tagCollectionTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tagCollectionTagService.update).toHaveBeenCalled();
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

    describe('compareTag', () => {
      it('Should forward to tagService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tagService, 'compareTag');
        comp.compareTag(entity, entity2);
        expect(tagService.compareTag).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
