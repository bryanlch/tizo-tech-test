const BASE_URL = "/api";

export const API_ENDPOINTS = {
  AUTH: {
    LOGIN: `${BASE_URL}/auth/login`,
    REGISTER: `${BASE_URL}/auth/register`,
  },
  BRANCHES: {
    BASE: `${BASE_URL}/branch`,
  },
  PRODUCTS: {
    BASE: `${BASE_URL}/product`,
  },
  INVENTORY: {
    BASE: `${BASE_URL}/inventory`,
    UPDATE: `${BASE_URL}/inventory/setting`,
  },
};
