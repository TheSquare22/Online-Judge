import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CompetitionProblemFormService, CompetitionProblemFormGroup } from './competition-problem-form.service';
import { ICompetitionProblem } from '../competition-problem.model';
import { CompetitionProblemService } from '../service/competition-problem.service';
import { IProblem } from 'app/entities/problem/problem.model';
import { ProblemService } from 'app/entities/problem/service/problem.service';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';

@Component({
  selector: 'jhi-competition-problem-update',
  templateUrl: './competition-problem-update.component.html',
})
export class CompetitionProblemUpdateComponent implements OnInit {
  isSaving = false;
  competitionProblem: ICompetitionProblem | null = null;

  problemsSharedCollection: IProblem[] = [];
  competitionsSharedCollection: ICompetition[] = [];

  editForm: CompetitionProblemFormGroup = this.competitionProblemFormService.createCompetitionProblemFormGroup();

  constructor(
    protected competitionProblemService: CompetitionProblemService,
    protected competitionProblemFormService: CompetitionProblemFormService,
    protected problemService: ProblemService,
    protected competitionService: CompetitionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProblem = (o1: IProblem | null, o2: IProblem | null): boolean => this.problemService.compareProblem(o1, o2);

  compareCompetition = (o1: ICompetition | null, o2: ICompetition | null): boolean => this.competitionService.compareCompetition(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competitionProblem }) => {
      this.competitionProblem = competitionProblem;
      if (competitionProblem) {
        this.updateForm(competitionProblem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const competitionProblem = this.competitionProblemFormService.getCompetitionProblem(this.editForm);
    if (competitionProblem.id !== null) {
      this.subscribeToSaveResponse(this.competitionProblemService.update(competitionProblem));
    } else {
      this.subscribeToSaveResponse(this.competitionProblemService.create(competitionProblem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompetitionProblem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(competitionProblem: ICompetitionProblem): void {
    this.competitionProblem = competitionProblem;
    this.competitionProblemFormService.resetForm(this.editForm, competitionProblem);

    this.problemsSharedCollection = this.problemService.addProblemToCollectionIfMissing<IProblem>(
      this.problemsSharedCollection,
      competitionProblem.problem
    );
    this.competitionsSharedCollection = this.competitionService.addCompetitionToCollectionIfMissing<ICompetition>(
      this.competitionsSharedCollection,
      competitionProblem.competition
    );
  }

  protected loadRelationshipsOptions(): void {
    this.problemService
      .query()
      .pipe(map((res: HttpResponse<IProblem[]>) => res.body ?? []))
      .pipe(
        map((problems: IProblem[]) =>
          this.problemService.addProblemToCollectionIfMissing<IProblem>(problems, this.competitionProblem?.problem)
        )
      )
      .subscribe((problems: IProblem[]) => (this.problemsSharedCollection = problems));

    this.competitionService
      .query()
      .pipe(map((res: HttpResponse<ICompetition[]>) => res.body ?? []))
      .pipe(
        map((competitions: ICompetition[]) =>
          this.competitionService.addCompetitionToCollectionIfMissing<ICompetition>(competitions, this.competitionProblem?.competition)
        )
      )
      .subscribe((competitions: ICompetition[]) => (this.competitionsSharedCollection = competitions));
  }
}
