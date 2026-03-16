import { Component, input, output, OnInit, signal } from "@angular/core";
import { CommonModule } from "@angular/common";

@Component({
  selector: "app-modal",
  standalone: true,
  imports: [CommonModule],
  templateUrl: "./modal.component.html",
})
export class ModalComponent implements OnInit {
  isOpen = input.required<boolean>();
  title = input<string>("Mensaje del Sistema");

  color = input<string>();

  close = output<void>();

  headerColor = signal<string>("bg-neo-primary");

  private readonly neoColors = [
    "bg-neo-primary",
    "bg-neo-secondary",
    "bg-neo-accent",
    "bg-neo-pink",
    "bg-neo-green",
  ];

  ngOnInit() {
    if (this.color()) {
      this.headerColor.set(this.color()!);
    } else {
      const randomColor =
        this.neoColors[Math.floor(Math.random() * this.neoColors.length)];
      this.headerColor.set(randomColor);
    }
  }

  onClose() {
    this.close.emit();
  }
}
