import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompetitionProblemComponent } from '../list/competition-problem.component';
import { CompetitionProblemDetailComponent } from '../detail/competition-problem-detail.component';
import { CompetitionProblemUpdateComponent } from '../update/competition-problem-update.component';
import { CompetitionProblemRoutingResolveService } from './competition-problem-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const competitionProblemRoute: Routes = [
  {
    path: '',
    component: CompetitionProblemComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompetitionProblemDetailComponent,
    resolve: {
      competitionProblem: CompetitionProblemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompetitionProblemUpdateComponent,
    resolve: {
      competitionProblem: CompetitionProblemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompetitionProblemUpdateComponent,
    resolve: {
      competitionProblem: CompetitionProblemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(competitionProblemRoute)],
  exports: [RouterModule],
})
export class CompetitionProblemRoutingModule {}
