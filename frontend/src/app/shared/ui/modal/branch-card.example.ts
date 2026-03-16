import { Component, inject } from '@angular/core';
import { NeoButtonDirective } from '@shared/ui';
import {
  NeoCardBodyComponent,
  NeoCardComponent,
  NeoCardFooterComponent,
  NeoCardHeaderComponent,
} from '@shared/ui';
import {
  ConfirmModalComponent,
  ConfirmModalData,
  ModalRef,
  ModalService,
} from '@shared/ui';

/**
 * Example feature component demonstrating:
 *  - NeoCard usage with header / body / footer slots
 *  - ModalService.open() with typed data and result
 */
@Component({
  selector: 'app-branch-card',
  standalone: true,
  imports: [
    NeoButtonDirective,
    NeoCardComponent,
    NeoCardHeaderComponent,
    NeoCardBodyComponent,
    NeoCardFooterComponent,
  ],
  template: `
    <neo-card>
      <neo-card-header>
        <h2 class="text-lg font-black">Downtown Branch</h2>
      </neo-card-header>

      <neo-card-body>
        <p class="font-medium">123 Main Street, Downtown</p>
        <p class="text-sm text-gray-500 mt-1">Active · 3 products in stock</p>
      </neo-card-body>

      <neo-card-footer>
        <button neoButton variant="white" size="sm" (click)="onEdit()">Edit</button>
        <button neoButton variant="pink" size="sm" (click)="onDelete()">Delete</button>
      </neo-card-footer>
    </neo-card>
  `,
})
export class BranchCardComponent {
  private readonly modalService = inject(ModalService);

  protected onEdit(): void {
    // Example: open a modal without data (no confirm needed)
    console.log('Edit triggered — open an edit modal here');
  }

  protected async onDelete(): Promise<void> {
    const ref: ModalRef<boolean, ConfirmModalData> =
      this.modalService.open<boolean, ConfirmModalData>(ConfirmModalComponent, {
        data: {
          title: 'Delete Branch',
          message: 'Are you sure you want to delete "Downtown"? This action cannot be undone.',
          confirmLabel: 'Yes, delete',
          cancelLabel: 'Cancel',
          variant: 'danger',
        },
      });

    const confirmed = await ref.result;

    if (confirmed) {
      console.log('Branch deleted!');
      // Call your delete service here.
    }
  }
}
