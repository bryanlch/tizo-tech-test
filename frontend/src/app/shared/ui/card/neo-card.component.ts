import { Component } from "@angular/core";

@Component({
  selector: "neo-card-header",
  standalone: true,
  template: `
    <div class="px-6 pt-6 pb-4 border-b-4 border-neo-black">
      <ng-content></ng-content>
    </div>
  `,
})
export class NeoCardHeaderComponent {}

@Component({
  selector: "neo-card-body",
  standalone: true,
  template: `
    <div class="px-6 py-4">
      <ng-content></ng-content>
    </div>
  `,
})
export class NeoCardBodyComponent {}

@Component({
  selector: "neo-card-footer",
  standalone: true,
  template: `
    <div
      class="px-6 pb-6 pt-4 border-t-4 border-neo-black flex items-center justify-end gap-3"
    >
      <ng-content></ng-content>
    </div>
  `,
})
export class NeoCardFooterComponent {}

@Component({
  selector: "neo-card",
  standalone: true,
  template: `
    <div class="bg-white border-4 border-neo-black shadow-neo overflow-hidden">
      <ng-content></ng-content>
    </div>
  `,
})
export class NeoCardComponent {}
