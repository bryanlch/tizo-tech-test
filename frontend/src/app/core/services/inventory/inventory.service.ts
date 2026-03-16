import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { ApiResponse } from "@core/models/api-response.model";
import {
  Inventory,
  InventoryUpdateRequest,
  InventoyByProduct,
} from "@core/models/inventory.model";
import { API_ENDPOINTS } from "@core/constants/api.constants";

@Injectable({
  providedIn: "root",
})
export class InventoryService {
  constructor(private http: HttpClient) {}

  getByBranch(branchId: number): Observable<ApiResponse<InventoyByProduct[]>> {
    return this.http.get<ApiResponse<InventoyByProduct[]>>(
      `${API_ENDPOINTS.INVENTORY.BASE}/branch/${branchId}`,
    );
  }

  updateStock(request: InventoryUpdateRequest): Observable<ApiResponse<Inventory>> {
    return this.http.post<ApiResponse<Inventory>>(
      API_ENDPOINTS.INVENTORY.UPDATE,
      request,
    );
  }
}
