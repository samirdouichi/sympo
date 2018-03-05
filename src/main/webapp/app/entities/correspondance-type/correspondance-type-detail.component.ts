import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CorrespondanceType } from './correspondance-type.model';
import { CorrespondanceTypeService } from './correspondance-type.service';

@Component({
    selector: 'jhi-correspondance-type-detail',
    templateUrl: './correspondance-type-detail.component.html'
})
export class CorrespondanceTypeDetailComponent implements OnInit, OnDestroy {

    correspondanceType: CorrespondanceType;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private correspondanceTypeService: CorrespondanceTypeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCorrespondanceTypes();
    }

    load(id) {
        this.correspondanceTypeService.find(id)
            .subscribe((correspondanceTypeResponse: HttpResponse<CorrespondanceType>) => {
                this.correspondanceType = correspondanceTypeResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCorrespondanceTypes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'correspondanceTypeListModification',
            (response) => this.load(this.correspondanceType.id)
        );
    }
}
