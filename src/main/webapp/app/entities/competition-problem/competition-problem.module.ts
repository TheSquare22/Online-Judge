import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompetitionProblemComponent } from './list/competition-problem.component';
import { CompetitionProblemDetailComponent } from './detail/competition-problem-detail.component';
import { CompetitionProblemUpdateComponent } from './update/competition-problem-update.component';
import { CompetitionProblemDeleteDialogComponent } from './delete/competition-problem-delete-dialog.component';
import { CompetitionProblemRoutingModule } from './route/competition-problem-routing.module';

@NgModule({
  imports: [SharedModule, CompetitionProblemRoutingModule],
  declarations: [
    CompetitionProblemComponent,
    CompetitionProblemDetailComponent,
    CompetitionProblemUpdateComponent,
    CompetitionProblemDeleteDialogComponent,
  ],
})
export class CompetitionProblemModule {}
