import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { DossierInstruction } from './dossier-instruction.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<DossierInstruction>;

@Injectable()
export class DossierInstructionService {

    private resourceUrl =  SERVER_API_URL + 'api/dossier-instructions';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/dossier-instructions';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(dossierInstruction: DossierInstruction): Observable<EntityResponseType> {
        const copy = this.convert(dossierInstruction);
        return this.http.post<DossierInstruction>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(dossierInstruction: DossierInstruction): Observable<EntityResponseType> {
        const copy = this.convert(dossierInstruction);
        return this.http.put<DossierInstruction>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<DossierInstruction>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<DossierInstruction[]>> {
        const options = createRequestOption(req);
        return this.http.get<DossierInstruction[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DossierInstruction[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<DossierInstruction[]>> {
        const options = createRequestOption(req);
        return this.http.get<DossierInstruction[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DossierInstruction[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: DossierInstruction = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<DossierInstruction[]>): HttpResponse<DossierInstruction[]> {
        const jsonResponse: DossierInstruction[] = res.body;
        const body: DossierInstruction[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to DossierInstruction.
     */
    private convertItemFromServer(dossierInstruction: DossierInstruction): DossierInstruction {
        const copy: DossierInstruction = Object.assign({}, dossierInstruction);
        copy.dateCreationInstruction = this.dateUtils
            .convertLocalDateFromServer(dossierInstruction.dateCreationInstruction);
        return copy;
    }

    /**
     * Convert a DossierInstruction to a JSON which can be sent to the server.
     */
    private convert(dossierInstruction: DossierInstruction): DossierInstruction {
        const copy: DossierInstruction = Object.assign({}, dossierInstruction);
        copy.dateCreationInstruction = this.dateUtils
            .convertLocalDateToServer(dossierInstruction.dateCreationInstruction);
        return copy;
    }
}
