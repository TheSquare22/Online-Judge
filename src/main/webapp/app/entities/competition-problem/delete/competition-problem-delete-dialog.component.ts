import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompetitionProblem } from '../competition-problem.model';
import { CompetitionProblemService } from '../service/competition-problem.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './competition-problem-delete-dialog.component.html',
})
export class CompetitionProblemDeleteDialogComponent {
  competitionProblem?: ICompetitionProblem;

  constructor(protected competitionProblemService: CompetitionProblemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.competitionProblemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
