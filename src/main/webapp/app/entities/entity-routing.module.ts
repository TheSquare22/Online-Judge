import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'competition',
        data: { pageTitle: 'arenaApp.competition.home.title' },
        loadChildren: () => import('./competition/competition.module').then(m => m.CompetitionModule),
      },
      {
        path: 'tag-collection',
        data: { pageTitle: 'arenaApp.tagCollection.home.title' },
        loadChildren: () => import('./tag-collection/tag-collection.module').then(m => m.TagCollectionModule),
      },
      {
        path: 'tag',
        data: { pageTitle: 'arenaApp.tag.home.title' },
        loadChildren: () => import('./tag/tag.module').then(m => m.TagModule),
      },
      {
        path: 'tag-collection-tag',
        data: { pageTitle: 'arenaApp.tagCollectionTag.home.title' },
        loadChildren: () => import('./tag-collection-tag/tag-collection-tag.module').then(m => m.TagCollectionTagModule),
      },
      {
        path: 'problem',
        data: { pageTitle: 'arenaApp.problem.home.title' },
        loadChildren: () => import('./problem/problem.module').then(m => m.ProblemModule),
      },
      {
        path: 'competition-problem',
        data: { pageTitle: 'arenaApp.competitionProblem.home.title' },
        loadChildren: () => import('./competition-problem/competition-problem.module').then(m => m.CompetitionProblemModule),
      },
      {
        path: 'submission',
        data: { pageTitle: 'arenaApp.submission.home.title' },
        loadChildren: () => import('./submission/submission.module').then(m => m.SubmissionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
