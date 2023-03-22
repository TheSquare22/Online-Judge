import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompetitionProblem, NewCompetitionProblem } from '../competition-problem.model';

export type PartialUpdateCompetitionProblem = Partial<ICompetitionProblem> & Pick<ICompetitionProblem, 'id'>;

export type EntityResponseType = HttpResponse<ICompetitionProblem>;
export type EntityArrayResponseType = HttpResponse<ICompetitionProblem[]>;

@Injectable({ providedIn: 'root' })
export class CompetitionProblemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/competition-problems');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(competitionProblem: NewCompetitionProblem): Observable<EntityResponseType> {
    return this.http.post<ICompetitionProblem>(this.resourceUrl, competitionProblem, { observe: 'response' });
  }

  update(competitionProblem: ICompetitionProblem): Observable<EntityResponseType> {
    return this.http.put<ICompetitionProblem>(
      `${this.resourceUrl}/${this.getCompetitionProblemIdentifier(competitionProblem)}`,
      competitionProblem,
      { observe: 'response' }
    );
  }

  partialUpdate(competitionProblem: PartialUpdateCompetitionProblem): Observable<EntityResponseType> {
    return this.http.patch<ICompetitionProblem>(
      `${this.resourceUrl}/${this.getCompetitionProblemIdentifier(competitionProblem)}`,
      competitionProblem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompetitionProblem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompetitionProblem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompetitionProblemIdentifier(competitionProblem: Pick<ICompetitionProblem, 'id'>): number {
    return competitionProblem.id;
  }

  compareCompetitionProblem(o1: Pick<ICompetitionProblem, 'id'> | null, o2: Pick<ICompetitionProblem, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompetitionProblemIdentifier(o1) === this.getCompetitionProblemIdentifier(o2) : o1 === o2;
  }

  addCompetitionProblemToCollectionIfMissing<Type extends Pick<ICompetitionProblem, 'id'>>(
    competitionProblemCollection: Type[],
    ...competitionProblemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const competitionProblems: Type[] = competitionProblemsToCheck.filter(isPresent);
    if (competitionProblems.length > 0) {
      const competitionProblemCollectionIdentifiers = competitionProblemCollection.map(
        competitionProblemItem => this.getCompetitionProblemIdentifier(competitionProblemItem)!
      );
      const competitionProblemsToAdd = competitionProblems.filter(competitionProblemItem => {
        const competitionProblemIdentifier = this.getCompetitionProblemIdentifier(competitionProblemItem);
        if (competitionProblemCollectionIdentifiers.includes(competitionProblemIdentifier)) {
          return false;
        }
        competitionProblemCollectionIdentifiers.push(competitionProblemIdentifier);
        return true;
      });
      return [...competitionProblemsToAdd, ...competitionProblemCollection];
    }
    return competitionProblemCollection;
  }
}
