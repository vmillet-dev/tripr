import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {TranslocoPipe} from "@jsverse/transloco";
import { environment } from 'src/environments/environment';


@Component({
  selector: 'app-home',
  imports: [CommonModule, TranslocoPipe],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  environment = environment;

}
