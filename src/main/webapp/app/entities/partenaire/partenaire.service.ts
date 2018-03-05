import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Partenaire } from './partenaire.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Partenaire>;

@Injectable()
export class PartenaireService {

    private resourceUrl =  SERVER_API_URL + 'api/partenaires';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/partenaires';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(partenaire: Partenaire): Observable<EntityResponseType> {
        const copy = this.convert(partenaire);
        return this.http.post<Partenaire>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(partenaire: Partenaire): Observable<EntityResponseType> {
        const copy = this.convert(partenaire);
        return this.http.put<Partenaire>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Partenaire>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Partenaire[]>> {
        const options = createRequestOption(req);
        return this.http.get<Partenaire[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Partenaire[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Partenaire[]>> {
        const options = createRequestOption(req);
        return this.http.get<Partenaire[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Partenaire[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Partenaire = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Partenaire[]>): HttpResponse<Partenaire[]> {
        const jsonResponse: Partenaire[] = res.body;
        const body: Partenaire[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Partenaire.
     */
    private convertItemFromServer(partenaire: Partenaire): Partenaire {
        const copy: Partenaire = Object.assign({}, partenaire);
        copy.resetDate = this.dateUtils
            .convertDateTimeFromServer(partenaire.resetDate);
        return copy;
    }

    /**
     * Convert a Partenaire to a JSON which can be sent to the server.
     */
    private convert(partenaire: Partenaire): Partenaire {
        const copy: Partenaire = Object.assign({}, partenaire);

        copy.resetDate = this.dateUtils.toDate(partenaire.resetDate);
        return copy;
    }
}
