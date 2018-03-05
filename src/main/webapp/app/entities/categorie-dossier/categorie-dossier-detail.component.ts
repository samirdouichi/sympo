import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CategorieDossier } from './categorie-dossier.model';
import { CategorieDossierService } from './categorie-dossier.service';

@Component({
    selector: 'jhi-categorie-dossier-detail',
    templateUrl: './categorie-dossier-detail.component.html'
})
export class CategorieDossierDetailComponent implements OnInit, OnDestroy {

    categorieDossier: CategorieDossier;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private categorieDossierService: CategorieDossierService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCategorieDossiers();
    }

    load(id) {
        this.categorieDossierService.find(id)
            .subscribe((categorieDossierResponse: HttpResponse<CategorieDossier>) => {
                this.categorieDossier = categorieDossierResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCategorieDossiers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'categorieDossierListModification',
            (response) => this.load(this.categorieDossier.id)
        );
    }
}
