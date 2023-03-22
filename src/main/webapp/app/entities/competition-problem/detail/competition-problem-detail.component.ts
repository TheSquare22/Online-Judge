import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICompetitionProblem } from '../competition-problem.model';

@Component({
  selector: 'jhi-competition-problem-detail',
  templateUrl: './competition-problem-detail.component.html',
})
export class CompetitionProblemDetailComponent implements OnInit {
  competitionProblem: ICompetitionProblem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competitionProblem }) => {
      this.competitionProblem = competitionProblem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
