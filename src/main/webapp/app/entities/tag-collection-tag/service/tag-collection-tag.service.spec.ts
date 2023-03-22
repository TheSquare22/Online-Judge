import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITagCollectionTag } from '../tag-collection-tag.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tag-collection-tag.test-samples';

import { TagCollectionTagService } from './tag-collection-tag.service';

const requireRestSample: ITagCollectionTag = {
  ...sampleWithRequiredData,
};

describe('TagCollectionTag Service', () => {
  let service: TagCollectionTagService;
  let httpMock: HttpTestingController;
  let expectedResult: ITagCollectionTag | ITagCollectionTag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TagCollectionTagService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a TagCollectionTag', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tagCollectionTag = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tagCollectionTag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TagCollectionTag', () => {
      const tagCollectionTag = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tagCollectionTag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TagCollectionTag', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TagCollectionTag', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TagCollectionTag', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTagCollectionTagToCollectionIfMissing', () => {
      it('should add a TagCollectionTag to an empty array', () => {
        const tagCollectionTag: ITagCollectionTag = sampleWithRequiredData;
        expectedResult = service.addTagCollectionTagToCollectionIfMissing([], tagCollectionTag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tagCollectionTag);
      });

      it('should not add a TagCollectionTag to an array that contains it', () => {
        const tagCollectionTag: ITagCollectionTag = sampleWithRequiredData;
        const tagCollectionTagCollection: ITagCollectionTag[] = [
          {
            ...tagCollectionTag,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTagCollectionTagToCollectionIfMissing(tagCollectionTagCollection, tagCollectionTag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TagCollectionTag to an array that doesn't contain it", () => {
        const tagCollectionTag: ITagCollectionTag = sampleWithRequiredData;
        const tagCollectionTagCollection: ITagCollectionTag[] = [sampleWithPartialData];
        expectedResult = service.addTagCollectionTagToCollectionIfMissing(tagCollectionTagCollection, tagCollectionTag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tagCollectionTag);
      });

      it('should add only unique TagCollectionTag to an array', () => {
        const tagCollectionTagArray: ITagCollectionTag[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tagCollectionTagCollection: ITagCollectionTag[] = [sampleWithRequiredData];
        expectedResult = service.addTagCollectionTagToCollectionIfMissing(tagCollectionTagCollection, ...tagCollectionTagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tagCollectionTag: ITagCollectionTag = sampleWithRequiredData;
        const tagCollectionTag2: ITagCollectionTag = sampleWithPartialData;
        expectedResult = service.addTagCollectionTagToCollectionIfMissing([], tagCollectionTag, tagCollectionTag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tagCollectionTag);
        expect(expectedResult).toContain(tagCollectionTag2);
      });

      it('should accept null and undefined values', () => {
        const tagCollectionTag: ITagCollectionTag = sampleWithRequiredData;
        expectedResult = service.addTagCollectionTagToCollectionIfMissing([], null, tagCollectionTag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tagCollectionTag);
      });

      it('should return initial array if no TagCollectionTag is added', () => {
        const tagCollectionTagCollection: ITagCollectionTag[] = [sampleWithRequiredData];
        expectedResult = service.addTagCollectionTagToCollectionIfMissing(tagCollectionTagCollection, undefined, null);
        expect(expectedResult).toEqual(tagCollectionTagCollection);
      });
    });

    describe('compareTagCollectionTag', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTagCollectionTag(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTagCollectionTag(entity1, entity2);
        const compareResult2 = service.compareTagCollectionTag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTagCollectionTag(entity1, entity2);
        const compareResult2 = service.compareTagCollectionTag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTagCollectionTag(entity1, entity2);
        const compareResult2 = service.compareTagCollectionTag(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
