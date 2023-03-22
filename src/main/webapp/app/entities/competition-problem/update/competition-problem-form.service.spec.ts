import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../competition-problem.test-samples';

import { CompetitionProblemFormService } from './competition-problem-form.service';

describe('CompetitionProblem Form Service', () => {
  let service: CompetitionProblemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompetitionProblemFormService);
  });

  describe('Service methods', () => {
    describe('createCompetitionProblemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompetitionProblemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            order: expect.any(Object),
            problem: expect.any(Object),
            competition: expect.any(Object),
          })
        );
      });

      it('passing ICompetitionProblem should create a new form with FormGroup', () => {
        const formGroup = service.createCompetitionProblemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            order: expect.any(Object),
            problem: expect.any(Object),
            competition: expect.any(Object),
          })
        );
      });
    });

    describe('getCompetitionProblem', () => {
      it('should return NewCompetitionProblem for default CompetitionProblem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCompetitionProblemFormGroup(sampleWithNewData);

        const competitionProblem = service.getCompetitionProblem(formGroup) as any;

        expect(competitionProblem).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompetitionProblem for empty CompetitionProblem initial value', () => {
        const formGroup = service.createCompetitionProblemFormGroup();

        const competitionProblem = service.getCompetitionProblem(formGroup) as any;

        expect(competitionProblem).toMatchObject({});
      });

      it('should return ICompetitionProblem', () => {
        const formGroup = service.createCompetitionProblemFormGroup(sampleWithRequiredData);

        const competitionProblem = service.getCompetitionProblem(formGroup) as any;

        expect(competitionProblem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompetitionProblem should not enable id FormControl', () => {
        const formGroup = service.createCompetitionProblemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompetitionProblem should disable id FormControl', () => {
        const formGroup = service.createCompetitionProblemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
