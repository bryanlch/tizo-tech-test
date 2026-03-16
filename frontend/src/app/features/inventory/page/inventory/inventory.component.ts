import { Component, OnInit, inject, signal, computed, effect } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

import { InventoryCardComponent } from "./component/card/card.component";
import { InventoryTableComponent } from "./component/table/table.component";
import { BranchesService } from "@core/services/branches/branches.service";
import { ProductsService } from "@core/services/products/products.service";
import { InventoryService } from "@core/services/inventory/inventory.service";
import { Branch } from "@core/models/branches.model";
import { Product, ProductWithInventory } from "@core/models/products.model";
import { InventoyByProduct } from "@core/models/inventory.model";
import { BranchSelectorComponent } from "@features/inventory/page/inventory/component/branch-selector/branch-selector.component";

@Component({
  selector: "app-inventory",
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    InventoryCardComponent,
    InventoryTableComponent,
    BranchSelectorComponent,
  ],
  templateUrl: "./inventory.component.html",
})
export class InventoryComponent implements OnInit {
  private branchesService = inject(BranchesService);
  private productsService = inject(ProductsService);
  private inventoryService = inject(InventoryService);

  branches = signal<Branch[]>([]);
  products = signal<ProductWithInventory[]>([]);
  inventoryData = signal<InventoyByProduct[]>([]);

  selectedBranchId = signal<number | null>(null);

  loadingProductsWithInventory = signal(false);
  loadingInitial = signal(true);
  loadingInventory = signal(false);
  updatingStockFor = signal<number | null>(null);

  inventoryMap = computed(() => {
    const map = new Map<number, number>();

    this.inventoryData().forEach((inv) => {
      const productId = inv.productId;

      if (productId) {
        map.set(productId, inv.quantity);
      }
    });

    return map;
  });

  constructor() {
    effect(() => {
      const branchId = this.selectedBranchId();

      if (!branchId) {
        this.inventoryData.set([]);
        return;
      }

      this.loadingInventory.set(true);

      this.inventoryService.getByBranch(branchId).subscribe({
        next: (res: any) => {
          this.inventoryData.set(res.code === 200 || res.code === 201 ? res.data : []);
          this.loadingInventory.set(false);
        },
        error: () => {
          this.inventoryData.set([]);
          this.loadingInventory.set(false);
        },
      });
    });
  }

  ngOnInit(): void {
    this.loadInitialData();
  }

  loadInitialData(): void {
    this.branchesService.getAll().subscribe({
      next: (res: any) => {
        if (res.code === 200 || res.code === 201) {
          this.branches.set(res.data);
        }
      },
    });

    this.productsService.getProductsWithInventory().subscribe({
      next: (res: any) => {
        if (res.code === 200 || res.code === 201) {
          this.loadingProductsWithInventory.set(true);
          this.products.set(res.data);
        }
        this.loadingInitial.set(false);
      },
      error: () => this.loadingInitial.set(false),
    });
  }

  onBranchSelect(value: string): void {
    const branchId = Number(value);
    this.selectedBranchId.set(branchId || null);
  }

  getQuantityForProduct(productId: number): number {
    const qty = this.inventoryMap().get(productId);

    return qty ? qty : 0;
  }

  getTotalQuantity(product: ProductWithInventory): number {
    return (
      product.inventoryByBranch?.reduce((total, inv) => total + inv.quantity, 0) ?? 0
    );
  }

  updateQuantity(product: Product, change: number): void {
    const branchId = this.selectedBranchId();
    if (!branchId || !product.id) return;

    const currentQty = this.getQuantityForProduct(product.id);
    const newQty = currentQty + change;

    if (newQty < 0) return;

    this.updatingStockFor.set(product.id);

    const request = {
      productId: product.id,
      branchId,
      quantity: change,
    };

    this.inventoryService.updateStock(request).subscribe({
      next: (res: any) => {
        if (res.code === 200 || res.code === 201) {
          const updated = [...this.inventoryData()];

          const index = updated.findIndex(
            (inv) => inv.productId === product.id || inv.productId === product.id,
          );

          if (index !== -1) {
            updated[index].quantity = newQty;
          } else {
            updated.push({
              productId: product.id || 1,
              branchId: branchId,
              quantity: newQty,
              id: 0,
              branchName: "",
              productName: "",
              productSku: "",
            });
          }

          this.inventoryData.set(updated);
        }

        this.updatingStockFor.set(null);
      },
      error: () => this.updatingStockFor.set(null),
    });
  }
}
