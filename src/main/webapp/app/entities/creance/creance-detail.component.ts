import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Creance } from './creance.model';
import { CreanceService } from './creance.service';

@Component({
    selector: 'jhi-creance-detail',
    templateUrl: './creance-detail.component.html'
})
export class CreanceDetailComponent implements OnInit, OnDestroy {

    creance: Creance;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private creanceService: CreanceService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCreances();
    }

    load(id) {
        this.creanceService.find(id)
            .subscribe((creanceResponse: HttpResponse<Creance>) => {
                this.creance = creanceResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCreances() {
        this.eventSubscriber = this.eventManager.subscribe(
            'creanceListModification',
            (response) => this.load(this.creance.id)
        );
    }
}
