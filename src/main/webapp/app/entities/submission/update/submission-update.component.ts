import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SubmissionFormService, SubmissionFormGroup } from './submission-form.service';
import { ISubmission } from '../submission.model';
import { SubmissionService } from '../service/submission.service';
import { ITagCollection } from 'app/entities/tag-collection/tag-collection.model';
import { TagCollectionService } from 'app/entities/tag-collection/service/tag-collection.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICompetitionProblem } from 'app/entities/competition-problem/competition-problem.model';
import { CompetitionProblemService } from 'app/entities/competition-problem/service/competition-problem.service';

@Component({
  selector: 'jhi-submission-update',
  templateUrl: './submission-update.component.html',
})
export class SubmissionUpdateComponent implements OnInit {
  isSaving = false;
  submission: ISubmission | null = null;

  tagCollectionsSharedCollection: ITagCollection[] = [];
  usersSharedCollection: IUser[] = [];
  competitionProblemsSharedCollection: ICompetitionProblem[] = [];

  editForm: SubmissionFormGroup = this.submissionFormService.createSubmissionFormGroup();

  constructor(
    protected submissionService: SubmissionService,
    protected submissionFormService: SubmissionFormService,
    protected tagCollectionService: TagCollectionService,
    protected userService: UserService,
    protected competitionProblemService: CompetitionProblemService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTagCollection = (o1: ITagCollection | null, o2: ITagCollection | null): boolean =>
    this.tagCollectionService.compareTagCollection(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareCompetitionProblem = (o1: ICompetitionProblem | null, o2: ICompetitionProblem | null): boolean =>
    this.competitionProblemService.compareCompetitionProblem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ submission }) => {
      this.submission = submission;
      if (submission) {
        this.updateForm(submission);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const submission = this.submissionFormService.getSubmission(this.editForm);
    if (submission.id !== null) {
      this.subscribeToSaveResponse(this.submissionService.update(submission));
    } else {
      this.subscribeToSaveResponse(this.submissionService.create(submission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubmission>>): void {
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

  protected updateForm(submission: ISubmission): void {
    this.submission = submission;
    this.submissionFormService.resetForm(this.editForm, submission);

    this.tagCollectionsSharedCollection = this.tagCollectionService.addTagCollectionToCollectionIfMissing<ITagCollection>(
      this.tagCollectionsSharedCollection,
      submission.tags
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, submission.user);
    this.competitionProblemsSharedCollection =
      this.competitionProblemService.addCompetitionProblemToCollectionIfMissing<ICompetitionProblem>(
        this.competitionProblemsSharedCollection,
        submission.competitionProblem
      );
  }

  protected loadRelationshipsOptions(): void {
    this.tagCollectionService
      .query()
      .pipe(map((res: HttpResponse<ITagCollection[]>) => res.body ?? []))
      .pipe(
        map((tagCollections: ITagCollection[]) =>
          this.tagCollectionService.addTagCollectionToCollectionIfMissing<ITagCollection>(tagCollections, this.submission?.tags)
        )
      )
      .subscribe((tagCollections: ITagCollection[]) => (this.tagCollectionsSharedCollection = tagCollections));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.submission?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.competitionProblemService
      .query()
      .pipe(map((res: HttpResponse<ICompetitionProblem[]>) => res.body ?? []))
      .pipe(
        map((competitionProblems: ICompetitionProblem[]) =>
          this.competitionProblemService.addCompetitionProblemToCollectionIfMissing<ICompetitionProblem>(
            competitionProblems,
            this.submission?.competitionProblem
          )
        )
      )
      .subscribe((competitionProblems: ICompetitionProblem[]) => (this.competitionProblemsSharedCollection = competitionProblems));
  }
}
