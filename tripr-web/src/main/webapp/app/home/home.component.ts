import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { environment } from 'environments/environment';
import {TranslocoPipe} from "@jsverse/transloco";


@Component({
  selector: 'app-home',
  imports: [CommonModule, TranslocoPipe],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  environment = environment;

}
