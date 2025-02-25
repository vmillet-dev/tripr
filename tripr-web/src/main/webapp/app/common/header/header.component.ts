import { Component, inject } from '@angular/core';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthenticationService } from 'app/security/authentication.service';
import {TranslocoPipe} from "@jsverse/transloco";


@Component({
  selector: 'app-header',
  imports: [CommonModule, NgOptimizedImage, RouterLink, TranslocoPipe],
  templateUrl: './header.component.html'
})
export class HeaderComponent {

  authenticationService = inject(AuthenticationService);

}
