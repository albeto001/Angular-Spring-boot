import { Injectable } from '@angular/core';
import { Client } from './client';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  public clients: Client[];
  private urlEndPoint = 'http://localhost:8080/api';
  private httpHeader: HttpHeaders;
  constructor(private http: HttpClient) {
    this.httpHeader = new HttpHeaders({ 'Content-Type': 'application/json' });
  }

  public getClients(): Observable <Client[]> {

    return this.http.get<Client[]>(this.urlEndPoint + '/clients');
  }

  public getClientById(id: number): Observable <Client[]> {
    return this.http.get<Client[]>(`${this.urlEndPoint}/client/${id}`);
  }

  public create(client: Client): Observable <Client>{
    return this.http.post<Client>(`${this.urlEndPoint}/client`, client, {headers: this.httpHeader} );
  }

  public update(client: Client, id: number): Observable<Client> {
    return this.http.put<Client>(`${this.urlEndPoint}/client/${id}`, client, {headers: this.httpHeader});
  }

  public delete(id: number): Observable<object> {
    return this.http.delete(`${this.urlEndPoint}/client/${id}`, {headers: this.httpHeader});
  }
}