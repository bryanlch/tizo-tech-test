// sidebar-mobile.component.ts
import { Component, Input, Output, EventEmitter } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { NeoButtonDirective } from "@shared/ui/button/neo-button.directive";

@Component({
  selector: "app-sidebar-mobile",
  standalone: true,
  imports: [CommonModule, RouterModule, NeoButtonDirective],
  templateUrl: "./sidebar-mobile.component.html",
})
export class SidebarMobileComponent {
  @Input() isOpen = false;
  @Input() routes: any[] = [];
  @Output() closeMenu = new EventEmitter<void>();
  @Output() logoutAction = new EventEmitter<void>();
}
