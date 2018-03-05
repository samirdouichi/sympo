import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Cabinet } from './cabinet.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Cabinet>;

@Injectable()
export class CabinetService {

    private resourceUrl =  SERVER_API_URL + 'api/cabinets';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/cabinets';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(cabinet: Cabinet): Observable<EntityResponseType> {
        const copy = this.convert(cabinet);
        return this.http.post<Cabinet>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(cabinet: Cabinet): Observable<EntityResponseType> {
        const copy = this.convert(cabinet);
        return this.http.put<Cabinet>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Cabinet>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Cabinet[]>> {
        const options = createRequestOption(req);
        return this.http.get<Cabinet[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Cabinet[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Cabinet[]>> {
        const options = createRequestOption(req);
        return this.http.get<Cabinet[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Cabinet[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Cabinet = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Cabinet[]>): HttpResponse<Cabinet[]> {
        const jsonResponse: Cabinet[] = res.body;
        const body: Cabinet[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Cabinet.
     */
    private convertItemFromServer(cabinet: Cabinet): Cabinet {
        const copy: Cabinet = Object.assign({}, cabinet);
        copy.dateCreation = this.dateUtils
            .convertLocalDateFromServer(cabinet.dateCreation);
        return copy;
    }

    /**
     * Convert a Cabinet to a JSON which can be sent to the server.
     */
    private convert(cabinet: Cabinet): Cabinet {
        const copy: Cabinet = Object.assign({}, cabinet);
        copy.dateCreation = this.dateUtils
            .convertLocalDateToServer(cabinet.dateCreation);
        return copy;
    }
}
