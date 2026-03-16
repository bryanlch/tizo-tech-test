import { Component } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { NAVIGATION } from "@core/navigation/navigation.config";
import { NeoButtonDirective } from "@shared/ui/button/neo-button.directive";

@Component({
  selector: "app-dashboard",
  standalone: true,
  imports: [CommonModule, RouterLink, NeoButtonDirective],
  templateUrl: "./dashboard.component.html",
})
export class DashboardComponent {
  cards = NAVIGATION.filter((r) => r.description);
}
