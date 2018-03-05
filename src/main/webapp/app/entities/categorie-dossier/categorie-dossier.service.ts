import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { CategorieDossier } from './categorie-dossier.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CategorieDossier>;

@Injectable()
export class CategorieDossierService {

    private resourceUrl =  SERVER_API_URL + 'api/categorie-dossiers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/categorie-dossiers';

    constructor(private http: HttpClient) { }

    create(categorieDossier: CategorieDossier): Observable<EntityResponseType> {
        const copy = this.convert(categorieDossier);
        return this.http.post<CategorieDossier>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(categorieDossier: CategorieDossier): Observable<EntityResponseType> {
        const copy = this.convert(categorieDossier);
        return this.http.put<CategorieDossier>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<CategorieDossier>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<CategorieDossier[]>> {
        const options = createRequestOption(req);
        return this.http.get<CategorieDossier[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CategorieDossier[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<CategorieDossier[]>> {
        const options = createRequestOption(req);
        return this.http.get<CategorieDossier[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CategorieDossier[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CategorieDossier = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<CategorieDossier[]>): HttpResponse<CategorieDossier[]> {
        const jsonResponse: CategorieDossier[] = res.body;
        const body: CategorieDossier[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to CategorieDossier.
     */
    private convertItemFromServer(categorieDossier: CategorieDossier): CategorieDossier {
        const copy: CategorieDossier = Object.assign({}, categorieDossier);
        return copy;
    }

    /**
     * Convert a CategorieDossier to a JSON which can be sent to the server.
     */
    private convert(categorieDossier: CategorieDossier): CategorieDossier {
        const copy: CategorieDossier = Object.assign({}, categorieDossier);
        return copy;
    }
}
