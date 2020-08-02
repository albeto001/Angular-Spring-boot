import { Component, OnInit } from '@angular/core';
import { Client } from '../client';
import { ClientService } from '../client.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.css']
})
export class FormComponent implements OnInit {
  public title: string;
  public client: Client;

  constructor(
      private clientService: ClientService,
      private router: Router
    ){
    this.title = 'New Client';
    this.client = new Client();
   }

  ngOnInit(): void {
  }

  public save(): void{
    this.clientService.create(this.client).subscribe(
      (result) => this.router.navigate(['/clients'])
    );
  }

}
