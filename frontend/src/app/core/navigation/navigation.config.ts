import { NeoButtonVariant } from "@shared/ui/button/neo-button.directive";

export interface NavigationItem {
  path: string;
  label: string;
  emoji: string;
  description?: string;
  color?: NeoButtonVariant;
}

export const NAVIGATION: NavigationItem[] = [
  {
    path: "",
    label: "Home",
    emoji: "🏠",
  },
  {
    path: "/dashboard",
    label: "Dashboard",
    emoji: "📊",
  },
  {
    path: "/branches",
    label: "Sucursales",
    emoji: "🏢",
    description: "Gestiona las sucursales",
    color: "primary",
  },
  {
    path: "/products",
    label: "Productos",
    emoji: "📦",
    description: "Gestiona el catálogo",
    color: "secondary",
  },
  {
    path: "/inventory",
    label: "Inventario",
    emoji: "📊",
    description: "Stock por sucursal",
    color: "accent",
  },
];
