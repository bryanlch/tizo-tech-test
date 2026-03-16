// layout.component.ts
import { Component } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { AuthService } from "../services/auth/auth.service";
import { SidebarMobileComponent } from "./component/sidebar-mobile/sidebar-mobile.component";
import { SidebarDesktopComponent } from "./component/sidebar-desktop/sidebar-desktop.component";
import { NeoButtonDirective } from "@shared/ui/button/neo-button.directive";
import { NAVIGATION } from "@core/navigation/navigation.config";

@Component({
  selector: "app-layout",
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    SidebarMobileComponent,
    SidebarDesktopComponent,
    NeoButtonDirective,
  ],
  templateUrl: "./layout.component.html",
})
export class LayoutComponent {
  isMenuOpen = false;

  routes = NAVIGATION;

  constructor(private authService: AuthService) {}

  toggleMenu(): void {
    this.isMenuOpen = !this.isMenuOpen;
  }

  logout(): void {
    this.authService.logout();
  }
}
