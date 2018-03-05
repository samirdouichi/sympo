import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Debiteur } from './debiteur.model';
import { DebiteurService } from './debiteur.service';

@Component({
    selector: 'jhi-debiteur-detail',
    templateUrl: './debiteur-detail.component.html'
})
export class DebiteurDetailComponent implements OnInit, OnDestroy {

    debiteur: Debiteur;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private debiteurService: DebiteurService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDebiteurs();
    }

    load(id) {
        this.debiteurService.find(id)
            .subscribe((debiteurResponse: HttpResponse<Debiteur>) => {
                this.debiteur = debiteurResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDebiteurs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'debiteurListModification',
            (response) => this.load(this.debiteur.id)
        );
    }
}
