import {
  Component,
  ComponentRef,
  HostListener,
  OnInit,
  ViewChild,
  ViewContainerRef,
  effect,
  signal,
} from "@angular/core";
import { ModalRef } from "./modal-ref";

@Component({
  selector: "neo-modal-container",
  standalone: true,
  template: `
    @if (visible()) {
      <div
        class="fixed inset-0 z-50 flex items-center justify-center"
        role="dialog"
        aria-modal="true"
      >
        <div class="absolute inset-0 bg-neo-black/40" (click)="onBackdropClick()"></div>

        <div
          class="relative z-10 w-full max-w-lg mx-4
                 bg-white border-4 border-neo-black shadow-neo-lg
                 animate-in fade-in zoom-in-95 duration-150"
          role="document"
        >
          <ng-container #contentSlot></ng-container>
        </div>
      </div>
    }
  `,
})
export class ModalContainerComponent implements OnInit {
  modalRef!: ModalRef;

  closeOnBackdrop = true;
  protected visible = signal(true);

  @ViewChild("contentSlot", { read: ViewContainerRef, static: true })
  contentSlot!: ViewContainerRef;

  contentRef?: ComponentRef<unknown>;

  ngOnInit(): void {
    effect(() => {
      const open = this.modalRef.isOpen();
      if (!open) {
        this.visible.set(false);
        setTimeout(() => this.destroy(), 150);
      }
    });
  }

  protected onBackdropClick(): void {
    if (this.closeOnBackdrop) {
      this.modalRef.close();
    }
  }

  @HostListener("document:keydown.escape")
  protected onEscapeKey(): void {
    this.modalRef.close();
  }

  private destroy(): void {
    this.contentRef?.destroy();
  }
}
