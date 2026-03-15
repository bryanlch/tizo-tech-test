export interface AuthRequest {
  username?: string;
  password?: string;
}

export interface AuthResponse {
  token: string;
  username: string;
}

export interface User {
  username: string;
}
