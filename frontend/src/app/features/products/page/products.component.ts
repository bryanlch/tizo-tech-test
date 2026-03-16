import { Component, OnInit, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule, FormBuilder, Validators } from "@angular/forms";

import { ProductsService } from "@core/services/products/products.service";
import { Product } from "@core/models/products.model";

import {
  NeoButtonDirective,
  NeoFieldComponent,
  NeoInputDirective,
  ModalComponent,
} from "@shared/ui";

@Component({
  selector: "app-products",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NeoFieldComponent,
    NeoInputDirective,
    NeoButtonDirective,
    ModalComponent,
  ],
  templateUrl: "./products.component.html",
})
export class ProductsComponent implements OnInit {
  private productsService = inject(ProductsService);
  private fb = inject(FormBuilder);

  selectedProduct = signal<Product | null>(null);
  showModal = signal(false);
  products = signal<Product[]>([]);
  loading = signal(false);
  successMessage = signal("");
  errorMessage = signal("");

  productForm = this.fb.nonNullable.group({
    name: ["", [Validators.required]],
    sku: ["", [Validators.required]],
    price: [0, [Validators.required, Validators.min(0.01)]],
  });

  ngOnInit(): void {
    this.loadProducts();
  }

  openModalProduct(product: Product): void {
    this.selectedProduct.set(product);
    this.productForm.patchValue({
      name: product.name,
      sku: product.sku,
      price: product.price,
    });
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
    this.selectedProduct.set(null);
    this.productForm.reset(); // Limpia el form al cerrar
    this.successMessage.set("");
    this.errorMessage.set("");
  }

  loadProducts(): void {
    this.productsService.getProducts().subscribe({
      next: (res: any) =>
        res.code >= 200 && res.code < 300 && this.products.set(res.data),
      error: (err) => console.error("Error al cargar productos", err),
    });
  }

  saveProduct(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    const productData = this.productForm.getRawValue();
    const isEditing = !!this.selectedProduct();

    const request = isEditing
      ? this.productsService.updateProduct({
          id: this.selectedProduct()!.id!,
          ...productData,
        })
      : this.productsService.createProduct(productData);

    request.subscribe({
      next: (res: any) => {
        this.loading.set(false);
        if (res.code >= 200 && res.code < 300) {
          this.successMessage.set(isEditing ? "¡Actualizado!" : "¡Creado!");
          this.loadProducts();
          if (!isEditing) this.productForm.reset();
          if (isEditing) {
            setTimeout(() => {
              this.closeModal();
            }, 1500);
          }
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(err.error?.message || "Error en la operación");
      },
    });
  }
}
