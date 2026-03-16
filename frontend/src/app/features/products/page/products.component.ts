import { Component, OnInit, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule, FormBuilder, Validators } from "@angular/forms";

import { ProductsService } from "@core/services/products/products.service";
import { Product } from "@core/models/products.model";

import { NeoButtonDirective, NeoFieldComponent, NeoInputDirective } from "@shared/ui";

@Component({
  selector: "app-products",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NeoFieldComponent,
    NeoInputDirective,
    NeoButtonDirective,
  ],
  templateUrl: "./products.component.html",
})
export class ProductsComponent implements OnInit {
  private productsService = inject(ProductsService);
  private fb = inject(FormBuilder);

  products = signal<Product[]>([]);
  loading = signal(false);

  successMessage = signal("");
  errorMessage = signal("");

  form = this.fb.nonNullable.group({
    name: ["", [Validators.required]],
    sku: ["", [Validators.required]],
    price: [0, [Validators.required, Validators.min(0.01)]],
  });

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productsService.getProducts().subscribe({
      next: (res: any) => {
        if (res.code === 200 || res.code === 201) {
          this.products.set(res.data);
        }
      },
      error: (err) => {
        console.error("Error al cargar productos", err);
      },
    });
  }

  onSubmit(): void {
    this.successMessage.set("");
    this.errorMessage.set("");

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.errorMessage.set("Por favor completa todos los campos correctamente.");
      return;
    }

    this.loading.set(true);

    const product: Product = {
      name: this.form.value.name as string,
      sku: this.form.value.sku as string,
      price: this.form.value.price as number,
    };

    this.productsService.createProduct(product).subscribe({
      next: (res: any) => {
        this.loading.set(false);

        if (res.code === 200 || res.code === 201) {
          this.successMessage.set(res.message);

          this.products.update((list) => [...list, res.data]);

          this.form.reset({
            name: "",
            sku: "",
            price: 0,
          });
        } else {
          this.errorMessage.set(res.message);
        }
      },

      error: (err: any) => {
        this.loading.set(false);

        this.errorMessage.set(
          err.error?.message || "Error del servidor al crear el producto.",
        );
      },
    });
  }
}
