import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICompetitionProblem } from '../competition-problem.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../competition-problem.test-samples';

import { CompetitionProblemService } from './competition-problem.service';

const requireRestSample: ICompetitionProblem = {
  ...sampleWithRequiredData,
};

describe('CompetitionProblem Service', () => {
  let service: CompetitionProblemService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompetitionProblem | ICompetitionProblem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompetitionProblemService);
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

    it('should create a CompetitionProblem', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const competitionProblem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(competitionProblem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompetitionProblem', () => {
      const competitionProblem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(competitionProblem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompetitionProblem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompetitionProblem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CompetitionProblem', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompetitionProblemToCollectionIfMissing', () => {
      it('should add a CompetitionProblem to an empty array', () => {
        const competitionProblem: ICompetitionProblem = sampleWithRequiredData;
        expectedResult = service.addCompetitionProblemToCollectionIfMissing([], competitionProblem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(competitionProblem);
      });

      it('should not add a CompetitionProblem to an array that contains it', () => {
        const competitionProblem: ICompetitionProblem = sampleWithRequiredData;
        const competitionProblemCollection: ICompetitionProblem[] = [
          {
            ...competitionProblem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompetitionProblemToCollectionIfMissing(competitionProblemCollection, competitionProblem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompetitionProblem to an array that doesn't contain it", () => {
        const competitionProblem: ICompetitionProblem = sampleWithRequiredData;
        const competitionProblemCollection: ICompetitionProblem[] = [sampleWithPartialData];
        expectedResult = service.addCompetitionProblemToCollectionIfMissing(competitionProblemCollection, competitionProblem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(competitionProblem);
      });

      it('should add only unique CompetitionProblem to an array', () => {
        const competitionProblemArray: ICompetitionProblem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const competitionProblemCollection: ICompetitionProblem[] = [sampleWithRequiredData];
        expectedResult = service.addCompetitionProblemToCollectionIfMissing(competitionProblemCollection, ...competitionProblemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const competitionProblem: ICompetitionProblem = sampleWithRequiredData;
        const competitionProblem2: ICompetitionProblem = sampleWithPartialData;
        expectedResult = service.addCompetitionProblemToCollectionIfMissing([], competitionProblem, competitionProblem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(competitionProblem);
        expect(expectedResult).toContain(competitionProblem2);
      });

      it('should accept null and undefined values', () => {
        const competitionProblem: ICompetitionProblem = sampleWithRequiredData;
        expectedResult = service.addCompetitionProblemToCollectionIfMissing([], null, competitionProblem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(competitionProblem);
      });

      it('should return initial array if no CompetitionProblem is added', () => {
        const competitionProblemCollection: ICompetitionProblem[] = [sampleWithRequiredData];
        expectedResult = service.addCompetitionProblemToCollectionIfMissing(competitionProblemCollection, undefined, null);
        expect(expectedResult).toEqual(competitionProblemCollection);
      });
    });

    describe('compareCompetitionProblem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompetitionProblem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCompetitionProblem(entity1, entity2);
        const compareResult2 = service.compareCompetitionProblem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCompetitionProblem(entity1, entity2);
        const compareResult2 = service.compareCompetitionProblem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCompetitionProblem(entity1, entity2);
        const compareResult2 = service.compareCompetitionProblem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
