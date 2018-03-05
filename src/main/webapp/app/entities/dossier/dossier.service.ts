import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Dossier } from './dossier.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Dossier>;

@Injectable()
export class DossierService {

    private resourceUrl =  SERVER_API_URL + 'api/dossiers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/dossiers';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(dossier: Dossier): Observable<EntityResponseType> {
        const copy = this.convert(dossier);
        return this.http.post<Dossier>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(dossier: Dossier): Observable<EntityResponseType> {
        const copy = this.convert(dossier);
        return this.http.put<Dossier>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Dossier>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Dossier[]>> {
        const options = createRequestOption(req);
        return this.http.get<Dossier[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Dossier[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Dossier[]>> {
        const options = createRequestOption(req);
        return this.http.get<Dossier[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Dossier[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Dossier = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Dossier[]>): HttpResponse<Dossier[]> {
        const jsonResponse: Dossier[] = res.body;
        const body: Dossier[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Dossier.
     */
    private convertItemFromServer(dossier: Dossier): Dossier {
        const copy: Dossier = Object.assign({}, dossier);
        copy.dateCreation = this.dateUtils
            .convertLocalDateFromServer(dossier.dateCreation);
        copy.updatedAt = this.dateUtils
            .convertLocalDateFromServer(dossier.updatedAt);
        copy.dateCloture = this.dateUtils
            .convertLocalDateFromServer(dossier.dateCloture);
        return copy;
    }

    /**
     * Convert a Dossier to a JSON which can be sent to the server.
     */
    private convert(dossier: Dossier): Dossier {
        const copy: Dossier = Object.assign({}, dossier);
        copy.dateCreation = this.dateUtils
            .convertLocalDateToServer(dossier.dateCreation);
        copy.updatedAt = this.dateUtils
            .convertLocalDateToServer(dossier.updatedAt);
        copy.dateCloture = this.dateUtils
            .convertLocalDateToServer(dossier.dateCloture);
        return copy;
    }
}
