import { Injectable } from '@angular/core';
import { Client } from './client';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { formatDate } from '@angular/common';

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

  public getClients(page: number = 0): Observable <any> {
    return this.http.get(this.urlEndPoint + '/clients/page/' + page).pipe(
      map((response: any) => {
        let page = response as any;
        page.content = page.content.map((client: Client) => {
          client.name = client.name.toUpperCase();
          client.surname = client.surname.toUpperCase();
          client.createdAt = formatDate(client.createdAt, 'EEE dd/MMM/yyyy', 'en-us');
          return client;
        });
        return page;
      }),
      catchError(e => {
        console.error(e);
        return throwError(e);
      })
    );
  }

  public getClientById(id: number): Observable <Client[]> {
    return this.http.get<Client[]>(`${this.urlEndPoint}/client/${id}`).pipe(
      map(response => response as Client[]),
      catchError(e => {
        console.error(e);
        return throwError(e);
      })
    );
  }

  public create(client: Client): Observable <Client>{
    return this.http.post<Client>(`${this.urlEndPoint}/client`, client, {headers: this.httpHeader} ).pipe(
      map(response => response as Client),
      catchError(e => {
        console.error(e);
        return throwError(e);
      })
    );
  }

  public update(client: Client, id: number): Observable<Client> {
    return this.http.put<Client>(`${this.urlEndPoint}/client/${id}`, client, {headers: this.httpHeader}).pipe(
      map(response => response as Client),
      catchError(e => {
        console.error(e);
        return throwError(e);
      })
    );
  }

  public delete(id: number): Observable<object> {
    return this.http.delete(`${this.urlEndPoint}/client/${id}`, {headers: this.httpHeader}).pipe(
      catchError(e => {
        console.error(e);
        return throwError(e);
      })
    );
  }
}
