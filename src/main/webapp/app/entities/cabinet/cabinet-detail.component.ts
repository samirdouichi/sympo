import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Cabinet } from './cabinet.model';
import { CabinetService } from './cabinet.service';

@Component({
    selector: 'jhi-cabinet-detail',
    templateUrl: './cabinet-detail.component.html'
})
export class CabinetDetailComponent implements OnInit, OnDestroy {

    cabinet: Cabinet;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private cabinetService: CabinetService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCabinets();
    }

    load(id) {
        this.cabinetService.find(id)
            .subscribe((cabinetResponse: HttpResponse<Cabinet>) => {
                this.cabinet = cabinetResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCabinets() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cabinetListModification',
            (response) => this.load(this.cabinet.id)
        );
    }
}
