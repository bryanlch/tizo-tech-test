import { Component, Input } from "@angular/core";
import { CommonModule } from "@angular/common";
import { Product } from "@core/models/products.model";

@Component({
  selector: "app-inventory-table",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./table.component.html",
})
export class InventoryTableComponent {
  @Input() products: Product[] = [];
  @Input() getQuantity!: (productId: number) => number;
  @Input() updatingStockFor!: number | null;

  @Input() decrease!: (product: Product) => void;
  @Input() increase!: (product: Product) => void;
}
