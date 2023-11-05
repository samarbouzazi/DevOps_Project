import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StockService {
  private apiUrl = 'http://192.168.153.128:8088/stock';

  constructor(private http: HttpClient) {
  }

  public fetchAllData(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  public fetchData(id: any): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  public fetchQuantity(id: any): Observable<any> {
    return this.http.get(`${this.apiUrl}/quantity/${id}`);
  }

  public addStock(stock: any): Observable<any> {

    return this.http.post(`${this.apiUrl}`, stock);
  }


}