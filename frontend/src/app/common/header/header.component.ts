import { Component, inject } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { RouterLink } from '@angular/router';
import {TranslocoPipe} from "@jsverse/transloco";
import {AuthService} from "../../shared/services/auth.service";


@Component({
  selector: 'app-header',
  imports: [CommonModule, NgOptimizedImage, RouterLink, TranslocoPipe],
  templateUrl: './header.component.html'
})
export class HeaderComponent {

  authService = inject(AuthService);

}
