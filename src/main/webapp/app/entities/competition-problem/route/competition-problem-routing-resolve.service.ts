import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompetitionProblem } from '../competition-problem.model';
import { CompetitionProblemService } from '../service/competition-problem.service';

@Injectable({ providedIn: 'root' })
export class CompetitionProblemRoutingResolveService implements Resolve<ICompetitionProblem | null> {
  constructor(protected service: CompetitionProblemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompetitionProblem | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((competitionProblem: HttpResponse<ICompetitionProblem>) => {
          if (competitionProblem.body) {
            return of(competitionProblem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
