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
  public clients: Client[];
  public componentName: string;

  constructor( private clientService: ClientService) {
    this.componentName = 'Clients';
  }

  ngOnInit(): void {
    // this.clients = this.clientService.getClients();
    this.clientService.getClients().subscribe(
      (result) => {
        this.clients = result;
      }
    );
  }

  delete(client: Client): void{
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
          },
          (error: any) => {
            swal.fire('Error', error.error.message, 'warning');
          }
        );
      }
    });
  }

}
