import { Component, Input, ContentChild, AfterContentInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { NgControl } from "@angular/forms";

@Component({
  selector: "neo-field",
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="flex flex-col">
      @if (label) {
        <label class="font-bold mb-1">
          {{ label }}
          @if (required) {
            <span class="text-red-500">*</span>
          }
        </label>
      }

      <ng-content></ng-content>

      <div class="h-6 mt-1">
        @if (errorMessage) {
          <span class="text-red-500 font-bold text-sm"> * {{ errorMessage }} </span>
        }
      </div>
    </div>
  `,
})
export class NeoFieldComponent implements AfterContentInit {
  @Input() label?: string;

  @ContentChild(NgControl) control?: NgControl;

  required = false;
  errorMessage = "";

  ngAfterContentInit(): void {
    const validator = this.control?.control?.validator;

    if (validator) {
      const validation = validator({} as any);
      this.required = validation?.["required"] ?? false;
    }

    this.control?.statusChanges?.subscribe(() => {
      this.updateError();
    });

    this.updateError();
  }

  private updateError(): void {
    const c = this.control?.control;

    if (!c) return;

    if (c.invalid && (c.dirty || c.touched)) {
      if (c.errors?.["required"]) {
        this.errorMessage = "This field is required";
      } else {
        this.errorMessage = "Invalid field value";
      }
    } else {
      this.errorMessage = "";
    }
  }
}
