import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../common/product';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private baseUrl = 'http://localhost:8080/products';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getMyProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/my`, { headers: this.getHeaders() });
  }

  createProduct(product: Product, file: File | null): Observable<Product> {
    const formData = new FormData();
    formData.append('name', product.name);
    formData.append('price', product.price.toString());
    formData.append('categoryId', product.categoryId.toString());
    formData.append('description', product.description || '');

    if (file) {
      formData.append('file', file);
    }

    return this.http.post<Product>(`${this.baseUrl}`, formData, {
      headers: this.getHeadersForFormData()
    });
  }

  updateProduct(id: number, product: Product, file: File | null): Observable<Product> {
    const formData = new FormData();
    formData.append('name', product.name);
    formData.append('price', product.price.toString());
    formData.append('categoryId', product.categoryId.toString());
    formData.append('description', product.description || '');

    if (file) {
      formData.append('file', file);
    }

    return this.http.put<Product>(`${this.baseUrl}/${id}`, formData, {
      headers: this.getHeadersForFormData()
    });
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getHeaders() });
  }

  getProductsByAgent(agentId: number): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.baseUrl}/by-agent/${agentId}`, {
      headers: this.getHeaders()
    });
  }

  getProductsGroupedByCategory(agentId: number): Observable<{ [categoryName: string]: Product[] }> {
    return this.http.get<{ [categoryName: string]: Product[] }>(
      `${this.baseUrl}/by-agent/${agentId}/grouped`
    );
  }
  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/${id}`, {
      headers: this.getHeaders()
    });
  }
  getProductPublic(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/public/${id}`);
  }
  

  private getHeadersForFormData(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }
}
