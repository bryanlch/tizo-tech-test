import { Component, OnInit, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule, FormBuilder, Validators } from "@angular/forms";

import { Branch } from "@core/models/branches.model";
import { BranchesService } from "@core/services/branches/branches.service";

import { NeoFieldComponent } from "@ui/field/neo-field.component";
import { NeoInputDirective } from "@ui/input/neo-input.directive";
import { NeoButtonDirective } from "@ui/button/neo-button.directive";

@Component({
  selector: "app-branches",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NeoFieldComponent,
    NeoInputDirective,
    NeoButtonDirective,
  ],
  templateUrl: "./branches.component.html",
})
export class BranchesComponent implements OnInit {
  private branchesService = inject(BranchesService);
  private fb = inject(FormBuilder);

  branches = signal<Branch[]>([]);

  loading = signal(false);
  errorMessage = signal("");
  successMessage = signal("");

  form = this.fb.nonNullable.group({
    name: ["", [Validators.required, Validators.minLength(2)]],
    address: ["", [Validators.required, Validators.minLength(5)]],
  });

  ngOnInit(): void {
    this.loadBranches();
  }

  loadBranches(): void {
    this.branchesService.getAll().subscribe({
      next: (res: any) => {
        if (res.code === 200 || res.code === 201) {
          this.branches.set(res.data);
        }
      },
      error: (err) => {
        console.error("Error loading branches", err);
      },
    });
  }

  onSubmit(): void {
    this.errorMessage.set("");
    this.successMessage.set("");

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.errorMessage.set("Por favor completa todos los campos.");
      return;
    }

    this.loading.set(true);

    this.branchesService
      .create({
        name: this.form.value.name as string,
        address: this.form.value.address as string,
      })
      .subscribe({
        next: (res: any) => {
          this.loading.set(false);

          if (res.code === 200 || res.code === 201) {
            this.successMessage.set("Sucursal creada con éxito.");

            this.branches.update((b) => [...b, res.data]);

            this.form.reset();
          } else {
            this.errorMessage.set(res.message);
          }
        },

        error: (err: any) => {
          this.loading.set(false);

          this.errorMessage.set(err.error?.message || "Error del servidor.");
        },
      });
  }
}
