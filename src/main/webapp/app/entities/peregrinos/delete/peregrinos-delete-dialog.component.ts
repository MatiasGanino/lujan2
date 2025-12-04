import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPeregrinos } from '../peregrinos.model';
import { PeregrinosService } from '../service/peregrinos.service';

@Component({
  templateUrl: './peregrinos-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PeregrinosDeleteDialogComponent {
  peregrinos?: IPeregrinos;

  protected peregrinosService = inject(PeregrinosService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.peregrinosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
