import { Directive, Input, HostBinding } from "@angular/core";

export type NeoButtonVariant =
  | "primary"
  | "secondary"
  | "accent"
  | "pink"
  | "green"
  | "white"
  | "black"
  | "default";

export type NeoButtonSize = "sm" | "md" | "lg" | "xl";

@Directive({
  selector: "[neoButton]",
  standalone: true,
})
export class NeoButtonDirective {
  @Input() variant: NeoButtonVariant = "primary";
  @Input() size: NeoButtonSize = "md";
  @Input() fullWidth = false;

  private base =
    "font-black border-4 border-neo-black shadow-neo transition-all inline-block text-center hover:-translate-x-0.5 hover:-translate-y-0.5 hover:shadow-neo-hover active:translate-x-1 active:translate-y-1 active:shadow-none";

  @HostBinding("class")
  get classes(): string {
    return [
      this.base,
      this.variantClasses,
      this.sizeClasses,
      this.fullWidth ? "w-full" : "",
    ].join(" ");
  }

  get variantClasses(): string {
    const variants: Record<NeoButtonVariant, string> = {
      primary: "bg-neo-primary text-white",
      secondary: "bg-neo-secondary text-white",
      accent: "bg-neo-accent text-black",
      pink: "bg-neo-pink text-black",
      green: "bg-neo-green text-black",
      white: "bg-white text-neo-black",
      black: "bg-neo-black text-white",
      default: "bg-neo-bg text-neo-black",
    };
    return variants[this.variant];
  }

  get sizeClasses(): string {
    const sizes: Record<NeoButtonSize, string> = {
      sm: "px-4 py-2 text-sm",
      md: "px-6 py-3 text-base",
      lg: "px-8 py-4 text-lg",
      xl: "px-10 py-5 text-xl",
    };
    return sizes[this.size];
  }
}
