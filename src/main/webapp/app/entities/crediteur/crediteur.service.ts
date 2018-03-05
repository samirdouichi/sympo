import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Crediteur } from './crediteur.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Crediteur>;

@Injectable()
export class CrediteurService {

    private resourceUrl =  SERVER_API_URL + 'api/crediteurs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/crediteurs';

    constructor(private http: HttpClient) { }

    create(crediteur: Crediteur): Observable<EntityResponseType> {
        const copy = this.convert(crediteur);
        return this.http.post<Crediteur>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(crediteur: Crediteur): Observable<EntityResponseType> {
        const copy = this.convert(crediteur);
        return this.http.put<Crediteur>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Crediteur>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Crediteur[]>> {
        const options = createRequestOption(req);
        return this.http.get<Crediteur[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Crediteur[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Crediteur[]>> {
        const options = createRequestOption(req);
        return this.http.get<Crediteur[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Crediteur[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Crediteur = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Crediteur[]>): HttpResponse<Crediteur[]> {
        const jsonResponse: Crediteur[] = res.body;
        const body: Crediteur[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Crediteur.
     */
    private convertItemFromServer(crediteur: Crediteur): Crediteur {
        const copy: Crediteur = Object.assign({}, crediteur);
        return copy;
    }

    /**
     * Convert a Crediteur to a JSON which can be sent to the server.
     */
    private convert(crediteur: Crediteur): Crediteur {
        const copy: Crediteur = Object.assign({}, crediteur);
        return copy;
    }
}
