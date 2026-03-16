import { Component, input, output } from "@angular/core";
import { CommonModule } from "@angular/common";

@Component({
  selector: "app-branch-selector",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./branch-selector.component.html",
})
export class BranchSelectorComponent {
  branches = input.required<any[]>();
  label = input<string>("Selecciona una Sucursal");
  note = input<string>("Selecciona una sucursal para gestionar los productos.");

  branchSelected = output<string>();

  onSelect(event: Event) {
    const value = (event.target as HTMLSelectElement).value;
    this.branchSelected.emit(value);
  }
}
