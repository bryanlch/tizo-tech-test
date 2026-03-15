import { Component, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, NgForm } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../../../core/services/auth/auth.service";
import { AuthRequest } from "../../../core/models/auth.model";

@Component({
  selector: "app-auth",
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: "./auth.component.html",
})
export class AuthComponent {
  private authService = inject(AuthService);
  private router = inject(Router);

  isLoginMode = signal(true);
  loading = signal(false);
  errorMensaje = signal("");

  authRequest: AuthRequest = { username: "", password: "" };

  switchMode(): void {
    this.isLoginMode.update((mode) => !mode);
    this.errorMensaje.set("");
    this.authRequest = { username: "", password: "" };
  }

  onSubmit(form: NgForm): void {
    this.errorMensaje.set("");

    if (form.invalid) {
      this.errorMensaje.set("Por favor completa todos los campos.");
      return;
    }

    this.loading.set(true);

    const authObservable = this.isLoginMode()
      ? this.authService.login(this.authRequest)
      : this.authService.register(this.authRequest);

    authObservable.subscribe({
      next: (res: any) => {
        this.loading.set(false);
        if (res.success) {
          this.router.navigate(["/dashboard"]);
        } else {
          this.errorMensaje.set(res.message);
        }
      },
      error: (err: any) => {
        this.loading.set(false);
        if (err.status === 403 || err.status === 401) {
          this.errorMensaje.set("Credenciales incorrectas.");
        } else {
          this.errorMensaje.set(
            err.error?.message || "Ocurrió un error en la autenticación.",
          );
        }
      },
    });
  }
}
