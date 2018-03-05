import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { CorrespondanceDocument } from './correspondance-document.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<CorrespondanceDocument>;

@Injectable()
export class CorrespondanceDocumentService {

    private resourceUrl =  SERVER_API_URL + 'api/correspondance-documents';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/correspondance-documents';

    constructor(private http: HttpClient) { }

    create(correspondanceDocument: CorrespondanceDocument): Observable<EntityResponseType> {
        const copy = this.convert(correspondanceDocument);
        return this.http.post<CorrespondanceDocument>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(correspondanceDocument: CorrespondanceDocument): Observable<EntityResponseType> {
        const copy = this.convert(correspondanceDocument);
        return this.http.put<CorrespondanceDocument>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<CorrespondanceDocument>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<CorrespondanceDocument[]>> {
        const options = createRequestOption(req);
        return this.http.get<CorrespondanceDocument[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CorrespondanceDocument[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<CorrespondanceDocument[]>> {
        const options = createRequestOption(req);
        return this.http.get<CorrespondanceDocument[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<CorrespondanceDocument[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: CorrespondanceDocument = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<CorrespondanceDocument[]>): HttpResponse<CorrespondanceDocument[]> {
        const jsonResponse: CorrespondanceDocument[] = res.body;
        const body: CorrespondanceDocument[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to CorrespondanceDocument.
     */
    private convertItemFromServer(correspondanceDocument: CorrespondanceDocument): CorrespondanceDocument {
        const copy: CorrespondanceDocument = Object.assign({}, correspondanceDocument);
        return copy;
    }

    /**
     * Convert a CorrespondanceDocument to a JSON which can be sent to the server.
     */
    private convert(correspondanceDocument: CorrespondanceDocument): CorrespondanceDocument {
        const copy: CorrespondanceDocument = Object.assign({}, correspondanceDocument);
        return copy;
    }
}
