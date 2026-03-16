import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { NeoButtonDirective } from "@shared/ui";

@Component({
  selector: "app-sidebar-desktop",
  standalone: true,
  imports: [CommonModule, RouterModule, NeoButtonDirective],
  templateUrl: "./sidebar-desktop.component.html",
})
export class SidebarDesktopComponent {
  @Input() routes: any[] = [];
  @Output() logoutAction = new EventEmitter<void>();
}
