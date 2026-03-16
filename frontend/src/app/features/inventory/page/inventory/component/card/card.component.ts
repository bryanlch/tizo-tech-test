import { Component, Input } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Product } from "@core/models/products.model";

@Component({
  selector: "app-inventory-card",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./card.component.html",
})
export class InventoryCardComponent {
  @Input() product!: Product;
  @Input() quantity = 0;
  @Input() loading = false;

  @Input() decrease!: (product: Product) => void;
  @Input() increase!: (product: Product) => void;
}
