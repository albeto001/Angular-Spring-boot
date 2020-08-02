import { Injectable } from '@angular/core';
import { Client } from './client';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHandler } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  public clients: Client[];
  private urlEndPoint = 'http://localhost:8080/api';
  constructor(private http: HttpClient) { }

  public getClients(): Observable <Client[]> {

    return this.http.get<Client[]>(this.urlEndPoint + '/clients');
  }

  public getClientById(id: number): Observable <Client[]> {
    return this.http.get<Client[]>(`${this.urlEndPoint}/client/${id}`);
  }

  public create(client: Client): Observable <Client>{
    return this.http.post<Client>(`${this.urlEndPoint}/client`, client);
  }

  public update(client: Client): Observable<Client> {
    return this.http.put<Client>(`${this.urlEndPoint}/client`, client);
  }

  public delete(id: number): void{
    this.http.delete(`${this.urlEndPoint}/client/${id}`);
  }
}
