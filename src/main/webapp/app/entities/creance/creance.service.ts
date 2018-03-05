import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Creance } from './creance.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Creance>;

@Injectable()
export class CreanceService {

    private resourceUrl =  SERVER_API_URL + 'api/creances';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/creances';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(creance: Creance): Observable<EntityResponseType> {
        const copy = this.convert(creance);
        return this.http.post<Creance>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(creance: Creance): Observable<EntityResponseType> {
        const copy = this.convert(creance);
        return this.http.put<Creance>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Creance>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Creance[]>> {
        const options = createRequestOption(req);
        return this.http.get<Creance[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Creance[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Creance[]>> {
        const options = createRequestOption(req);
        return this.http.get<Creance[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Creance[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Creance = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Creance[]>): HttpResponse<Creance[]> {
        const jsonResponse: Creance[] = res.body;
        const body: Creance[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Creance.
     */
    private convertItemFromServer(creance: Creance): Creance {
        const copy: Creance = Object.assign({}, creance);
        copy.createdAt = this.dateUtils
            .convertLocalDateFromServer(creance.createdAt);
        copy.updatedAt = this.dateUtils
            .convertLocalDateFromServer(creance.updatedAt);
        return copy;
    }

    /**
     * Convert a Creance to a JSON which can be sent to the server.
     */
    private convert(creance: Creance): Creance {
        const copy: Creance = Object.assign({}, creance);
        copy.createdAt = this.dateUtils
            .convertLocalDateToServer(creance.createdAt);
        copy.updatedAt = this.dateUtils
            .convertLocalDateToServer(creance.updatedAt);
        return copy;
    }
}
