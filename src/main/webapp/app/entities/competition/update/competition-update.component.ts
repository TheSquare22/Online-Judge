import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CompetitionFormService, CompetitionFormGroup } from './competition-form.service';
import { ICompetition } from '../competition.model';
import { CompetitionService } from '../service/competition.service';

@Component({
  selector: 'jhi-competition-update',
  templateUrl: './competition-update.component.html',
})
export class CompetitionUpdateComponent implements OnInit {
  isSaving = false;
  competition: ICompetition | null = null;

  competitionsSharedCollection: ICompetition[] = [];

  editForm: CompetitionFormGroup = this.competitionFormService.createCompetitionFormGroup();

  constructor(
    protected competitionService: CompetitionService,
    protected competitionFormService: CompetitionFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCompetition = (o1: ICompetition | null, o2: ICompetition | null): boolean => this.competitionService.compareCompetition(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competition }) => {
      this.competition = competition;
      if (competition) {
        this.updateForm(competition);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const competition = this.competitionFormService.getCompetition(this.editForm);
    if (competition.id !== null) {
      this.subscribeToSaveResponse(this.competitionService.update(competition));
    } else {
      this.subscribeToSaveResponse(this.competitionService.create(competition));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompetition>>): void {
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

  protected updateForm(competition: ICompetition): void {
    this.competition = competition;
    this.competitionFormService.resetForm(this.editForm, competition);

    this.competitionsSharedCollection = this.competitionService.addCompetitionToCollectionIfMissing<ICompetition>(
      this.competitionsSharedCollection,
      competition.parent
    );
  }

  protected loadRelationshipsOptions(): void {
    this.competitionService
      .query()
      .pipe(map((res: HttpResponse<ICompetition[]>) => res.body ?? []))
      .pipe(
        map((competitions: ICompetition[]) =>
          this.competitionService.addCompetitionToCollectionIfMissing<ICompetition>(competitions, this.competition?.parent)
        )
      )
      .subscribe((competitions: ICompetition[]) => (this.competitionsSharedCollection = competitions));
  }
}
