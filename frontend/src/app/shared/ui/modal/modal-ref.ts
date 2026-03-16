import { signal } from '@angular/core';

export const MODAL_REF = Symbol('MODAL_REF');

/**
 * Typed handle returned to the opener of a modal.
 *
 * @template R  Type of the value the modal resolves with when closed.
 * @template D  Type of the data passed into the modal.
 */
export class ModalRef<R = unknown, D = unknown> {
  readonly data: D;

  readonly isOpen = signal(true);

  private _resolve!: (value: R | undefined) => void;

  readonly result: Promise<R | undefined>;

  constructor(data: D) {
    this.data = data;
    this.result = new Promise<R | undefined>((resolve) => {
      this._resolve = resolve;
    });
  }

  close(result?: R): void {
    this.isOpen.set(false);
    this._resolve(result);
  }
}
