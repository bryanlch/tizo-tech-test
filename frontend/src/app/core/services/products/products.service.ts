import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Product, ProductWithInventory } from "../../models/products.model";
import { ApiResponse } from "../../models/api-response.model";
import { API_ENDPOINTS } from "../../constants/api.constants";

@Injectable({
  providedIn: "root",
})
export class ProductsService {
  constructor(private http: HttpClient) {}

  getProductsWithInventory(): Observable<ApiResponse<ProductWithInventory[]>> {
    return this.http.get<ApiResponse<ProductWithInventory[]>>(
      API_ENDPOINTS.PRODUCTS.WITH_INVENTORY,
    );
  }

  getProducts(): Observable<ApiResponse<Product[]>> {
    return this.http.get<ApiResponse<Product[]>>(API_ENDPOINTS.PRODUCTS.BASE);
  }

  createProduct(product: Product): Observable<ApiResponse<Product>> {
    return this.http.post<ApiResponse<Product>>(API_ENDPOINTS.PRODUCTS.BASE, product);
  }

  updateProduct(product: Product): Observable<ApiResponse<Product>> {
    return this.http.put<ApiResponse<Product>>(
      `${API_ENDPOINTS.PRODUCTS.BASE}/${product.id}`,
      product,
    );
  }

  deleteProduct(productId: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
      `${API_ENDPOINTS.PRODUCTS.BASE}/${productId}`,
    );
  }
}
