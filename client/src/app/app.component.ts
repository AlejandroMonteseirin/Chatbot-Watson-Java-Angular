import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'client';
  users=['ChatBotCBD','Usted'];
  messages=[{'sender':'ChatBotCBD','text':'Saludos Bienvenido a su aprobado en cbd'}]
  message:any;
  username:any;

  addUser(){
      console.log("Cambiado Nombre")
      this.users.pop();
      this.users.push(this.username);
  }
}
