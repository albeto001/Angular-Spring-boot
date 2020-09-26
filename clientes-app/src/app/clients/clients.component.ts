import { Component, OnInit } from '@angular/core';
import { Client } from './client';
import { ClientService } from './client.service';
import swal from 'sweetalert2';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: []
})
export class ClientsComponent implements OnInit {
  public clients: Client[] = [];
  public componentName: string;
  public pager: any;

  constructor( private clientService: ClientService) {
    this.componentName = 'Clients';
  }

  ngOnInit(): void {
    // this.clients = this.clientService.getClients();
    this.getPage();
  }

  public getPage = (page: number = 0): void => {
    this.clientService.getClients(page).subscribe(
      (result) => {
        this.clients = result.content as Client[];
        delete result.content;
        this.pager = result;
      }
    );
  }

  public delete(client: Client): void{
    swal.fire({
      title: 'Are you sure?',
      text: 'You won\'t be able to revert this!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!'
    }).then((res) => {
      if (res.value){
        this.clientService.delete(client.id).subscribe(
          () => {
            swal.fire('Deleted', `The Client '${client.name}' was deleted`, 'success');
            this.clients = this.clients.filter(cli => cli !== client);
            this.updatePageDone();
          },
          (error: any) => {
            swal.fire('Error', error.error.message, 'error');
          }
        );
      }
    });
  }

  private updatePageDone(): void{
    if (this.clients.length === 0){
      if (!(this.pager.last && this.pager.first)){
        if (this.pager.last){
          this.getPage(this.pager.number - 1);
        } else{
          this.getPage(this.pager.number);
        }
      }
    }
  }

}
