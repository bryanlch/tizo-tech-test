import {
  ApplicationRef,
  ComponentRef,
  Injectable,
  Injector,
  Type,
  createComponent,
} from '@angular/core';
import { ModalRef, MODAL_REF } from './modal-ref';
import { ModalContainerComponent } from './modal-container.component';

/** Options accepted by ModalService.open(). */
export interface ModalOpenOptions<D> {
  /** Arbitrary data forwarded to the modal component via ModalRef.data. */
  data?: D;
  /** If false, clicking the backdrop will NOT close the modal. Default: true. */
  closeOnBackdrop?: boolean;
}

/**
 * Service for opening modals dynamically, similar to Angular Material's MatDialog.
 *
 * @example
 * ```ts
 * const ref = this.modalService.open(ConfirmModalComponent, { data: { message: 'Delete?' } });
 * const result = await ref.result;
 * if (result) { ... }
 * ```
 */
@Injectable({ providedIn: 'root' })
export class ModalService {
  constructor(
    private readonly appRef: ApplicationRef,
    private readonly injector: Injector,
  ) {}

  /**
   * Dynamically renders `component` inside a ModalContainerComponent appended to document.body.
   *
   * @param component  Standalone component to render as modal content.
   * @param options    Optional data and behaviour settings.
   * @returns          ModalRef instance for controlling the modal and observing its result.
   */
  open<R = unknown, D = unknown>(
    component: Type<unknown>,
    options: ModalOpenOptions<D> = {},
  ): ModalRef<R, D> {
    const modalRef = new ModalRef<R, D>(options.data as D);

    // Build an injector that provides ModalRef so the inner component can inject it.
    const contentInjector = Injector.create({
      parent: this.injector,
      providers: [{ provide: MODAL_REF, useValue: modalRef }],
    });

    // Create the container component attached to the ApplicationRef.
    const containerRef: ComponentRef<ModalContainerComponent> = createComponent(
      ModalContainerComponent,
      {
        environmentInjector: this.appRef.injector,
        elementInjector: this.injector,
      },
    );

    // Wire the container before it initialises.
    containerRef.instance.modalRef = modalRef as ModalRef;
    containerRef.instance.closeOnBackdrop = options.closeOnBackdrop ?? true;

    // Attach container to Angular's change detection tree.
    this.appRef.attachView(containerRef.hostView);

    // Append the container's DOM node to document.body.
    const domElem = containerRef.location.nativeElement as HTMLElement;
    document.body.appendChild(domElem);

    // Dynamically create the content component inside the container's ViewContainerRef.
    const contentRef = containerRef.instance.contentSlot.createComponent(
      component,
      { injector: contentInjector },
    );
    containerRef.instance.contentRef = contentRef;

    // When the modal closes, clean up the container from the DOM and change detection.
    modalRef.result.finally(() => {
      setTimeout(() => {
        this.appRef.detachView(containerRef.hostView);
        containerRef.destroy();
        if (domElem.parentNode) {
          domElem.parentNode.removeChild(domElem);
        }
      }, 200);
    });

    containerRef.changeDetectorRef.detectChanges();

    return modalRef;
  }
}
