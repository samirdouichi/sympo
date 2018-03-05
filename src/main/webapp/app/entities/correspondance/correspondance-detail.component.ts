import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Correspondance } from './correspondance.model';
import { CorrespondanceService } from './correspondance.service';

@Component({
    selector: 'jhi-correspondance-detail',
    templateUrl: './correspondance-detail.component.html'
})
export class CorrespondanceDetailComponent implements OnInit, OnDestroy {

    correspondance: Correspondance;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private correspondanceService: CorrespondanceService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCorrespondances();
    }

    load(id) {
        this.correspondanceService.find(id)
            .subscribe((correspondanceResponse: HttpResponse<Correspondance>) => {
                this.correspondance = correspondanceResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCorrespondances() {
        this.eventSubscriber = this.eventManager.subscribe(
            'correspondanceListModification',
            (response) => this.load(this.correspondance.id)
        );
    }
}
