import { Component, OnInit } from '@angular/core';
import { Client } from './client';
import { ClientService } from './client.service';

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
      (result) => this.clients = result
    );
  }

}
