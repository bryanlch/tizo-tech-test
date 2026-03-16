import { Component, input, output } from "@angular/core";
import { CommonModule } from "@angular/common";

@Component({
  selector: "app-product-card",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./product-card.component.html",
})
export class ProductCardComponent {
  product = input.required<any>();

  bgColor = input<string>("bg-white");

  selected = output<any>();
}
