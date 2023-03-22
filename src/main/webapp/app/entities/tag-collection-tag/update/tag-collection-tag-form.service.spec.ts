import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tag-collection-tag.test-samples';

import { TagCollectionTagFormService } from './tag-collection-tag-form.service';

describe('TagCollectionTag Form Service', () => {
  let service: TagCollectionTagFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TagCollectionTagFormService);
  });

  describe('Service methods', () => {
    describe('createTagCollectionTagFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTagCollectionTagFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            collection: expect.any(Object),
            tag: expect.any(Object),
          })
        );
      });

      it('passing ITagCollectionTag should create a new form with FormGroup', () => {
        const formGroup = service.createTagCollectionTagFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            collection: expect.any(Object),
            tag: expect.any(Object),
          })
        );
      });
    });

    describe('getTagCollectionTag', () => {
      it('should return NewTagCollectionTag for default TagCollectionTag initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTagCollectionTagFormGroup(sampleWithNewData);

        const tagCollectionTag = service.getTagCollectionTag(formGroup) as any;

        expect(tagCollectionTag).toMatchObject(sampleWithNewData);
      });

      it('should return NewTagCollectionTag for empty TagCollectionTag initial value', () => {
        const formGroup = service.createTagCollectionTagFormGroup();

        const tagCollectionTag = service.getTagCollectionTag(formGroup) as any;

        expect(tagCollectionTag).toMatchObject({});
      });

      it('should return ITagCollectionTag', () => {
        const formGroup = service.createTagCollectionTagFormGroup(sampleWithRequiredData);

        const tagCollectionTag = service.getTagCollectionTag(formGroup) as any;

        expect(tagCollectionTag).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITagCollectionTag should not enable id FormControl', () => {
        const formGroup = service.createTagCollectionTagFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTagCollectionTag should disable id FormControl', () => {
        const formGroup = service.createTagCollectionTagFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
