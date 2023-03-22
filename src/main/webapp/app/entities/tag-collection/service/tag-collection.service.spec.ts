import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITagCollection } from '../tag-collection.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tag-collection.test-samples';

import { TagCollectionService } from './tag-collection.service';

const requireRestSample: ITagCollection = {
  ...sampleWithRequiredData,
};

describe('TagCollection Service', () => {
  let service: TagCollectionService;
  let httpMock: HttpTestingController;
  let expectedResult: ITagCollection | ITagCollection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TagCollectionService);
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

    it('should create a TagCollection', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tagCollection = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tagCollection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TagCollection', () => {
      const tagCollection = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tagCollection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TagCollection', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TagCollection', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TagCollection', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTagCollectionToCollectionIfMissing', () => {
      it('should add a TagCollection to an empty array', () => {
        const tagCollection: ITagCollection = sampleWithRequiredData;
        expectedResult = service.addTagCollectionToCollectionIfMissing([], tagCollection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tagCollection);
      });

      it('should not add a TagCollection to an array that contains it', () => {
        const tagCollection: ITagCollection = sampleWithRequiredData;
        const tagCollectionCollection: ITagCollection[] = [
          {
            ...tagCollection,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTagCollectionToCollectionIfMissing(tagCollectionCollection, tagCollection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TagCollection to an array that doesn't contain it", () => {
        const tagCollection: ITagCollection = sampleWithRequiredData;
        const tagCollectionCollection: ITagCollection[] = [sampleWithPartialData];
        expectedResult = service.addTagCollectionToCollectionIfMissing(tagCollectionCollection, tagCollection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tagCollection);
      });

      it('should add only unique TagCollection to an array', () => {
        const tagCollectionArray: ITagCollection[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tagCollectionCollection: ITagCollection[] = [sampleWithRequiredData];
        expectedResult = service.addTagCollectionToCollectionIfMissing(tagCollectionCollection, ...tagCollectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tagCollection: ITagCollection = sampleWithRequiredData;
        const tagCollection2: ITagCollection = sampleWithPartialData;
        expectedResult = service.addTagCollectionToCollectionIfMissing([], tagCollection, tagCollection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tagCollection);
        expect(expectedResult).toContain(tagCollection2);
      });

      it('should accept null and undefined values', () => {
        const tagCollection: ITagCollection = sampleWithRequiredData;
        expectedResult = service.addTagCollectionToCollectionIfMissing([], null, tagCollection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tagCollection);
      });

      it('should return initial array if no TagCollection is added', () => {
        const tagCollectionCollection: ITagCollection[] = [sampleWithRequiredData];
        expectedResult = service.addTagCollectionToCollectionIfMissing(tagCollectionCollection, undefined, null);
        expect(expectedResult).toEqual(tagCollectionCollection);
      });
    });

    describe('compareTagCollection', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTagCollection(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTagCollection(entity1, entity2);
        const compareResult2 = service.compareTagCollection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTagCollection(entity1, entity2);
        const compareResult2 = service.compareTagCollection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTagCollection(entity1, entity2);
        const compareResult2 = service.compareTagCollection(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
