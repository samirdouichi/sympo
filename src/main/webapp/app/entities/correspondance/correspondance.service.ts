import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Correspondance } from './correspondance.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Correspondance>;

@Injectable()
export class CorrespondanceService {

    private resourceUrl =  SERVER_API_URL + 'api/correspondances';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/correspondances';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(correspondance: Correspondance): Observable<EntityResponseType> {
        const copy = this.convert(correspondance);
        return this.http.post<Correspondance>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(correspondance: Correspondance): Observable<EntityResponseType> {
        const copy = this.convert(correspondance);
        return this.http.put<Correspondance>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Correspondance>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Correspondance[]>> {
        const options = createRequestOption(req);
        return this.http.get<Correspondance[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Correspondance[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Correspondance[]>> {
        const options = createRequestOption(req);
        return this.http.get<Correspondance[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Correspondance[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Correspondance = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Correspondance[]>): HttpResponse<Correspondance[]> {
        const jsonResponse: Correspondance[] = res.body;
        const body: Correspondance[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Correspondance.
     */
    private convertItemFromServer(correspondance: Correspondance): Correspondance {
        const copy: Correspondance = Object.assign({}, correspondance);
        copy.createdAt = this.dateUtils
            .convertLocalDateFromServer(correspondance.createdAt);
        copy.updatedAt = this.dateUtils
            .convertLocalDateFromServer(correspondance.updatedAt);
        return copy;
    }

    /**
     * Convert a Correspondance to a JSON which can be sent to the server.
     */
    private convert(correspondance: Correspondance): Correspondance {
        const copy: Correspondance = Object.assign({}, correspondance);
        copy.createdAt = this.dateUtils
            .convertLocalDateToServer(correspondance.createdAt);
        copy.updatedAt = this.dateUtils
            .convertLocalDateToServer(correspondance.updatedAt);
        return copy;
    }
}
