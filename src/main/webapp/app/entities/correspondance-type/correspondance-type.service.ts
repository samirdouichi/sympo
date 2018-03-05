import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { CorrespondanceType } from './correspondance-type.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CorrespondanceType>;

@Injectable()
export class CorrespondanceTypeService {

    private resourceUrl =  SERVER_API_URL + 'api/correspondance-types';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/correspondance-types';

    constructor(private http: HttpClient) { }

    create(correspondanceType: CorrespondanceType): Observable<EntityResponseType> {
        const copy = this.convert(correspondanceType);
        return this.http.post<CorrespondanceType>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(correspondanceType: CorrespondanceType): Observable<EntityResponseType> {
        const copy = this.convert(correspondanceType);
        return this.http.put<CorrespondanceType>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<CorrespondanceType>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<CorrespondanceType[]>> {
        const options = createRequestOption(req);
        return this.http.get<CorrespondanceType[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CorrespondanceType[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<CorrespondanceType[]>> {
        const options = createRequestOption(req);
        return this.http.get<CorrespondanceType[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CorrespondanceType[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CorrespondanceType = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<CorrespondanceType[]>): HttpResponse<CorrespondanceType[]> {
        const jsonResponse: CorrespondanceType[] = res.body;
        const body: CorrespondanceType[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to CorrespondanceType.
     */
    private convertItemFromServer(correspondanceType: CorrespondanceType): CorrespondanceType {
        const copy: CorrespondanceType = Object.assign({}, correspondanceType);
        return copy;
    }

    /**
     * Convert a CorrespondanceType to a JSON which can be sent to the server.
     */
    private convert(correspondanceType: CorrespondanceType): CorrespondanceType {
        const copy: CorrespondanceType = Object.assign({}, correspondanceType);
        return copy;
    }
}
