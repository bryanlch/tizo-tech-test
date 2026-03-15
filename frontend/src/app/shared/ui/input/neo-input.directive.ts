import { Directive, HostBinding, Input } from "@angular/core";

export type NeoInputSize = "sm" | "md" | "lg";
export type NeoInputVariant =
  | "default"
  | "primary"
  | "secondary"
  | "accent"
  | "pink"
  | "green"
  | "black"
  | "white";

@Directive({
  selector: "input[neoInput], textarea[neoInput]",
  standalone: true,
})
export class NeoInputDirective {
  @Input() size: NeoInputSize = "md";
  @Input() variant: NeoInputVariant = "default";
  @Input() fullWidth = true;

  private base =
    "border-4 border-neo-black shadow-neo font-bold outline-none transition-all focus:translate-x-0.5 focus:translate-y-0.5 focus:shadow-neo-hover";

  @HostBinding("class")
  get classes(): string {
    return [
      this.base,
      this.sizeClasses,
      this.variantClasses,
      this.fullWidth ? "w-full" : "",
    ].join(" ");
  }

  get sizeClasses(): string {
    const sizes: Record<NeoInputSize, string> = {
      sm: "px-3 py-1 text-sm",
      md: "px-4 py-2 text-base",
      lg: "px-5 py-3 text-lg",
    };

    return sizes[this.size];
  }

  get variantClasses(): string {
    const variants: Record<NeoInputVariant, string> = {
      default: "bg-white text-black",
      primary: "bg-neo-primary text-white",
      secondary: "bg-neo-secondary text-white",
      accent: "bg-neo-accent text-black",
      pink: "bg-neo-pink text-black",
      green: "bg-neo-green text-black",
      black: "bg-neo-black text-white",
      white: "bg-neo-white text-black",
    };

    return variants[this.variant];
  }
}
