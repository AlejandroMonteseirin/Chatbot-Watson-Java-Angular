import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private http:HttpClient) {
     
  }
  

  title = 'client';
  users=['ChatBotCBD','Usted'];
  messages=[{'sender':'ChatBotCBD','text':'Saludos bienvenido a ChatBotCBD'}]
  message:any;
  username:any;
  id=-1;
  addUser(){
      console.log("Cambiado Nombre")
      this.users.pop();
      this.users.push(this.username);
      this.messages.push({'sender':'ChatBotCBD','text':'Reiniciando conversacion...'})
      this.delay(1500).then(()=>{
        this.messages.push({'sender':'ChatBotCBD','text':'Saludos '+this.users[1]+" bienvenido al ChatBotCBD"})
      });
      this.id=-1;


  }
  private delay(ms: number)
    {
      return new Promise(resolve => setTimeout(resolve, ms));
    }
  chat(){
    this.messages.push({'sender':this.users[1],'text':this.message});
    console.log(this.id);
    this.http.post('http://localhost:8080/api/chats/talk',{'chatId':this.id,'text':this.message}).subscribe((data)=>{
      this.message='';
      console.log(data)
      this.id=data['chatId'];
      this.messages.push({'sender':'ChatBotCBD','text':data['text']})

    });
  }


}
