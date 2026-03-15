export interface Inventory {
  id?: number;
  product: any;
  branch: any;
  quantity: number;
}

export interface InventoyByProduct {
  id: number;
  branchId: number;
  branchName: string;
  productId: number;
  productName: string;
  productSku: string;
  quantity: number;
}

export interface InventoryUpdateRequest {
  productId: number;
  branchId: number;
  quantity: number;
}
