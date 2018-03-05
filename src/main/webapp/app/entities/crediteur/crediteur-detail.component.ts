import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Crediteur } from './crediteur.model';
import { CrediteurService } from './crediteur.service';

@Component({
    selector: 'jhi-crediteur-detail',
    templateUrl: './crediteur-detail.component.html'
})
export class CrediteurDetailComponent implements OnInit, OnDestroy {

    crediteur: Crediteur;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private crediteurService: CrediteurService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCrediteurs();
    }

    load(id) {
        this.crediteurService.find(id)
            .subscribe((crediteurResponse: HttpResponse<Crediteur>) => {
                this.crediteur = crediteurResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCrediteurs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'crediteurListModification',
            (response) => this.load(this.crediteur.id)
        );
    }
}
