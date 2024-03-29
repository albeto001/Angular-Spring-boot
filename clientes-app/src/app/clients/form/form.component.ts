import { Component, OnInit } from '@angular/core';
import { Client } from '../client';
import { ClientService } from '../client.service';
import { Router, ActivatedRoute } from '@angular/router';
import swal from 'sweetalert2';


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
      private router: Router,
      private activatedRoute: ActivatedRoute
    ){
    this.title = 'New Client';
    this.client = new Client();
   }

  ngOnInit(): void {
    this.loadClient();
  }

  private loadClient(): void{
    this.activatedRoute.params.subscribe(
      params => {
        const id: number = params.id;
        if (id){
          this.title = 'Edit Client';
          this.clientService.getClientById(id).subscribe(
            client => {
              this.client = client[0];
            },
            (error: any) => {
              console.error(error);
              swal.fire(error.status, error.error.message, 'error');
              this.router.navigate(['/clients']);
            }
          );
        }
      }
    );
  }

  public save(): void{
    if (this.client.id){
      this.clientService.update(this.client, this.client.id).subscribe(
        result => {
          console.log(result);
          this.router.navigate(['/clients']);
          swal.fire('Update', `The client ${this.client.name} has been updated`, 'success');
        },
        (error: any) => {
          this.handleSaveError(error);
        }
      );
    }
    else {
      this.clientService.create(this.client).subscribe(
        (result) => {
          this.router.navigate(['/clients']);
          swal.fire('New Client', `The client ${this.client.name} has been created`, 'success');
        },
        (error: any) => {
          this.handleSaveError(error);
        }
      );
    }
  }

  private handleSaveError(error: any): void{
    console.log(error);
    let msg = '';
    if (error.status === 400){
      error.error.forEach(element => {
        msg += element + '<hr>';
      });
    } else {
      msg = error.message;
    }
    swal.fire(error.status, msg, 'error');
  }

}
