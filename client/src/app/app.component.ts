import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'client';
  users=['ChatBotWapoWapo','Usted'];
  messages=[{'sender':'ChatBotWapoWapo','text':'Saludos Bienvenido a su aprobado en cbd'}]
  message:any;
  username:any;

  addUser(){
    console.log("Añadido")
  }
}
