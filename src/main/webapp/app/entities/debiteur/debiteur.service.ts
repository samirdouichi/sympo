import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Debiteur } from './debiteur.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Debiteur>;

@Injectable()
export class DebiteurService {

    private resourceUrl =  SERVER_API_URL + 'api/debiteurs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/debiteurs';

    constructor(private http: HttpClient) { }

    create(debiteur: Debiteur): Observable<EntityResponseType> {
        const copy = this.convert(debiteur);
        return this.http.post<Debiteur>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(debiteur: Debiteur): Observable<EntityResponseType> {
        const copy = this.convert(debiteur);
        return this.http.put<Debiteur>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Debiteur>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Debiteur[]>> {
        const options = createRequestOption(req);
        return this.http.get<Debiteur[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Debiteur[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Debiteur[]>> {
        const options = createRequestOption(req);
        return this.http.get<Debiteur[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Debiteur[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Debiteur = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Debiteur[]>): HttpResponse<Debiteur[]> {
        const jsonResponse: Debiteur[] = res.body;
        const body: Debiteur[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Debiteur.
     */
    private convertItemFromServer(debiteur: Debiteur): Debiteur {
        const copy: Debiteur = Object.assign({}, debiteur);
        return copy;
    }

    /**
     * Convert a Debiteur to a JSON which can be sent to the server.
     */
    private convert(debiteur: Debiteur): Debiteur {
        const copy: Debiteur = Object.assign({}, debiteur);
        return copy;
    }
}
