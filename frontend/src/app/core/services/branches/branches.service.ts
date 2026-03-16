import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Branch } from '../../models/branches.model';
import { ApiResponse } from '../../models/api-response.model';
import { API_ENDPOINTS } from '../../constants/api.constants';

@Injectable({
  providedIn: 'root'
})
export class BranchesService {

  constructor(private http: HttpClient) { }

  getAll(): Observable<ApiResponse<Branch[]>> {
    return this.http.get<ApiResponse<Branch[]>>(API_ENDPOINTS.BRANCHES.BASE);
  }

  create(branch: Branch): Observable<ApiResponse<Branch>> {
    return this.http.post<ApiResponse<Branch>>(API_ENDPOINTS.BRANCHES.BASE, branch);
  }
}
