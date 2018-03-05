import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Partenaire } from './partenaire.model';
import { PartenaireService } from './partenaire.service';

@Component({
    selector: 'jhi-partenaire-detail',
    templateUrl: './partenaire-detail.component.html'
})
export class PartenaireDetailComponent implements OnInit, OnDestroy {

    partenaire: Partenaire;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private partenaireService: PartenaireService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPartenaires();
    }

    load(id) {
        this.partenaireService.find(id)
            .subscribe((partenaireResponse: HttpResponse<Partenaire>) => {
                this.partenaire = partenaireResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPartenaires() {
        this.eventSubscriber = this.eventManager.subscribe(
            'partenaireListModification',
            (response) => this.load(this.partenaire.id)
        );
    }
}
