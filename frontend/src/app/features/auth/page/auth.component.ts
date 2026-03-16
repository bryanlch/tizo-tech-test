import { Component, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule, FormBuilder, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { NeoButtonDirective, NeoFieldComponent, NeoInputDirective } from "@shared/ui";
import { AuthService } from "@core/services/auth/auth.service";

@Component({
  selector: "app-auth",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NeoFieldComponent,
    NeoInputDirective,
    NeoButtonDirective,
  ],
  templateUrl: "./auth.component.html",
})
export class AuthComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  isLoginMode = signal(true);
  loading = signal(false);
  errorMensaje = signal("");

  form = this.fb.nonNullable.group({
    username: ["", Validators.required],
    password: ["", Validators.required],
  });

  switchMode(): void {
    this.isLoginMode.update((mode) => !mode);
    this.errorMensaje.set("");
    this.form.reset();
  }

  getError(controlName: string, message: string): string {
    const control = this.form.get(controlName);
    return control && control.invalid && control.touched ? message : "";
  }

  onSubmit(): void {
    this.errorMensaje.set("");

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);

    const authObservable = this.isLoginMode()
      ? this.authService.login(this.form.value)
      : this.authService.register(this.form.value);

    authObservable.subscribe({
      next: (res: any) => {
        this.loading.set(false);

        if (res.code === 200 || res.code === 201) {
          this.router.navigate(["/dashboard"]);
        } else {
          this.errorMensaje.set(res.message);
        }
      },
      error: (err: any) => {
        this.loading.set(false);

        if (err.status === 401 || err.status === 403) {
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
