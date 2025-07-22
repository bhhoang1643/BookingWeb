import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StyleTag } from '../common/styletag';

@Injectable({
  providedIn: 'root'
})
export class StyleTagService {
  private apiUrl = 'http://localhost:8080/style-tags';

  constructor(private http: HttpClient) {}

  getAllTags(): Observable<StyleTag[]> {
    return this.http.get<StyleTag[]>(this.apiUrl);
  }
}
