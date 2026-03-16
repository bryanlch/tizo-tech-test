import { Component, OnInit, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { ProductsService } from "@core/services/products/products.service";
import { AuthService } from "@core/services/auth/auth.service";
import { Product } from "@core/models/products.model";
import { NeoButtonDirective } from "@shared/ui/button/neo-button.directive";
import { ProductCardComponent } from "@features/home/page/component/product-card/product-card.component";

@Component({
  selector: "app-home",
  standalone: true,
  imports: [CommonModule, RouterModule, NeoButtonDirective, ProductCardComponent],
  templateUrl: "./home.component.html",
})
export class HomeComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly productsService = inject(ProductsService);

  products = signal<Product[] | null>(null);
  loading = signal(true);

  routes = [
    {
      path: "dashboard",
      label: "Dashboard",
    },
  ];

  ngOnInit(): void {
    this.loadProducts();
  }

  get isAuthenticated() {
    return this.authService.isAuthenticated();
  }

  loadProducts(): void {
    this.productsService.getProductsPublic().subscribe({
      next: (res: any) => {
        this.loading.set(false);
        if (res.code === 200 || res.code === 201) {
          this.products.set(res.data);
        }
      },
      error: (err: any) => {
        this.loading.set(false);
        console.error("Error al cargar productos publicos", err);
      },
    });
  }

  logout(): void {
    this.authService.logout();
  }
}
