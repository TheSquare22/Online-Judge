import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tag-collection.test-samples';

import { TagCollectionFormService } from './tag-collection-form.service';

describe('TagCollection Form Service', () => {
  let service: TagCollectionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TagCollectionFormService);
  });

  describe('Service methods', () => {
    describe('createTagCollectionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTagCollectionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });

      it('passing ITagCollection should create a new form with FormGroup', () => {
        const formGroup = service.createTagCollectionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
          })
        );
      });
    });

    describe('getTagCollection', () => {
      it('should return NewTagCollection for default TagCollection initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTagCollectionFormGroup(sampleWithNewData);

        const tagCollection = service.getTagCollection(formGroup) as any;

        expect(tagCollection).toMatchObject(sampleWithNewData);
      });

      it('should return NewTagCollection for empty TagCollection initial value', () => {
        const formGroup = service.createTagCollectionFormGroup();

        const tagCollection = service.getTagCollection(formGroup) as any;

        expect(tagCollection).toMatchObject({});
      });

      it('should return ITagCollection', () => {
        const formGroup = service.createTagCollectionFormGroup(sampleWithRequiredData);

        const tagCollection = service.getTagCollection(formGroup) as any;

        expect(tagCollection).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITagCollection should not enable id FormControl', () => {
        const formGroup = service.createTagCollectionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTagCollection should disable id FormControl', () => {
        const formGroup = service.createTagCollectionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
