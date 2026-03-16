import { computed, inject, Injectable, signal } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, tap } from "rxjs";
import { AuthRequest, AuthResponse, User } from "../../models/auth.model";
import { ApiResponse } from "../../models/api-response.model";
import { Router } from "@angular/router";
import { API_ENDPOINTS } from "../../constants/api.constants";

@Injectable({
  providedIn: "root",
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);

  private _user = signal<User | null>(null);
  private _token = signal<string | null>(null);

  user = computed<User | null>(() => this._user());
  isAuthenticated = computed<boolean>(() => !!this._token());
  private tokenKey = "sim_jwt_token";
  private userKey = "sim-user";

  constructor() {
    this.hydrateSession();
  }

  login(request: AuthRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http
      .post<ApiResponse<AuthResponse>>(API_ENDPOINTS.AUTH.LOGIN, request)
      .pipe(
        tap((res) => {
          if (res.code === 200 && res.data?.token) {
            this.setSession(res.data.token, res.data.username);
          }
        }),
      );
  }

  register(request: AuthRequest): Observable<ApiResponse<AuthResponse>> {
    return this.http
      .post<ApiResponse<AuthResponse>>(API_ENDPOINTS.AUTH.REGISTER, request)
      .pipe(
        tap((res) => {
          if (res.code === 200 && res.data?.token) {
            this.setSession(res.data.token, res.data.username);
          }
        }),
      );
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);

    this._token.set(null);
    this._user.set(null);

    this.router.navigate(["/"]);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  private setSession(token: string, user: string): void {
    localStorage.setItem(this.tokenKey, token);
    localStorage.setItem(this.userKey, JSON.stringify({ username: user }));

    this._token.set(token);
    this._user.set({ username: user });
  }

  private hydrateSession(): void {
    const token = localStorage.getItem(this.tokenKey);
    const userData = localStorage.getItem(this.userKey);
    if (token && userData) {
      this._token.set(token);
      this._user.set(JSON.parse(userData));
    }
  }
}
