import { Component, OnInit, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule, FormBuilder, Validators } from "@angular/forms";

import { Branch } from "@core/models/branches.model";
import { BranchesService } from "@core/services/branches/branches.service";

import { NeoFieldComponent } from "@ui/field/neo-field.component";
import { NeoInputDirective } from "@ui/input/neo-input.directive";
import { NeoButtonDirective } from "@ui/button/neo-button.directive";
import { ModalComponent } from "@shared/ui";

@Component({
  selector: "app-branches",
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NeoFieldComponent,
    NeoInputDirective,
    NeoButtonDirective,
    ModalComponent,
  ],
  templateUrl: "./branches.component.html",
})
export class BranchesComponent implements OnInit {
  private branchesService = inject(BranchesService);
  private fb = inject(FormBuilder);

  branches = signal<Branch[]>([]);
  selectedBranch = signal<Branch | null>(null);
  showModal = signal(false);
  loading = signal(false);
  errorMessage = signal("");
  successMessage = signal("");

  branchForm = this.fb.nonNullable.group({
    name: ["", [Validators.required, Validators.minLength(2)]],
    address: ["", [Validators.required, Validators.minLength(5)]],
  });

  ngOnInit(): void {
    this.loadBranches();
  }

  openModal(branch?: Branch): void {
    if (branch) {
      this.selectedBranch.set(branch);
      this.branchForm.patchValue({
        name: branch.name,
        address: branch.address,
      });
    } else {
      this.selectedBranch.set(null);
      this.branchForm.reset();
    }
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
    this.selectedBranch.set(null);
    this.branchForm.reset();
    this.errorMessage.set("");
    this.successMessage.set("");
  }

  loadBranches(): void {
    this.branchesService.getAll().subscribe({
      next: (res: any) => {
        if (res.code === 200 || res.code === 201) this.branches.set(res.data);
      },
      error: (err) => console.error("Error loading branches", err),
    });
  }

  saveBranch(): void {
    if (this.branchForm.invalid) {
      this.branchForm.markAllAsTouched();
      this.errorMessage.set("Por favor, revisa los campos marcados.");
      return;
    }

    this.loading.set(true);
    this.errorMessage.set("");
    const data = this.branchForm.getRawValue();
    const isEditing = !!this.selectedBranch();

    const request = isEditing
      ? this.branchesService.update({ id: this.selectedBranch()!.id, ...data })
      : this.branchesService.create(data);

    request.subscribe({
      next: (res: any) => {
        this.loading.set(false);
        if (res.code === 200 || res.code === 201) {
          this.successMessage.set(isEditing ? "Sucursal actualizada" : "Sucursal creada");
          this.loadBranches();
          if (!isEditing) this.branchForm.reset();
          if (isEditing) {
            setTimeout(() => {
              this.closeModal();
            }, 1500);
          }
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(err.error?.message || "Error en el servidor.");
      },
    });
  }
}
