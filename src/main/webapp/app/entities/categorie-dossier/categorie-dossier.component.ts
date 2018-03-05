import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CategorieDossier } from './categorie-dossier.model';
import { CategorieDossierService } from './categorie-dossier.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-categorie-dossier',
    templateUrl: './categorie-dossier.component.html'
})
export class CategorieDossierComponent implements OnInit, OnDestroy {
categorieDossiers: CategorieDossier[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private categorieDossierService: CategorieDossierService,
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
            this.categorieDossierService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: HttpResponse<CategorieDossier[]>) => this.categorieDossiers = res.body,
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
       }
        this.categorieDossierService.query().subscribe(
            (res: HttpResponse<CategorieDossier[]>) => {
                this.categorieDossiers = res.body;
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
        this.registerChangeInCategorieDossiers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: CategorieDossier) {
        return item.id;
    }
    registerChangeInCategorieDossiers() {
        this.eventSubscriber = this.eventManager.subscribe('categorieDossierListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
