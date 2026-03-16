export interface Product {
  id?: number;
  name: string;
  sku: string;
  price: number;
}

export interface ProductWithInventory extends Product {
  inventoryByBranch?: {
    branchId: number;
    branchName: string;
    quantity: number;
  }[];
}
