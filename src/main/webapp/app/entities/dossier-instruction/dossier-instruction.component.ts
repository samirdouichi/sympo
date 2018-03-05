import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { DossierInstruction } from './dossier-instruction.model';
import { DossierInstructionService } from './dossier-instruction.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-dossier-instruction',
    templateUrl: './dossier-instruction.component.html'
})
export class DossierInstructionComponent implements OnInit, OnDestroy {
dossierInstructions: DossierInstruction[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private dossierInstructionService: DossierInstructionService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ?
            this.activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.dossierInstructionService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<DossierInstruction[]>) => this.dossierInstructions = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.dossierInstructionService.query().subscribe(
            (res: HttpResponse<DossierInstruction[]>) => {
                this.dossierInstructions = res.body;
                this.currentSearch = '';
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInDossierInstructions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: DossierInstruction) {
        return item.id;
    }
    registerChangeInDossierInstructions() {
        this.eventSubscriber = this.eventManager.subscribe('dossierInstructionListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
