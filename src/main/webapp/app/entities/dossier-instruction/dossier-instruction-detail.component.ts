import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { DossierInstruction } from './dossier-instruction.model';
import { DossierInstructionService } from './dossier-instruction.service';

@Component({
    selector: 'jhi-dossier-instruction-detail',
    templateUrl: './dossier-instruction-detail.component.html'
})
export class DossierInstructionDetailComponent implements OnInit, OnDestroy {

    dossierInstruction: DossierInstruction;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dossierInstructionService: DossierInstructionService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDossierInstructions();
    }

    load(id) {
        this.dossierInstructionService.find(id)
            .subscribe((dossierInstructionResponse: HttpResponse<DossierInstruction>) => {
                this.dossierInstruction = dossierInstructionResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDossierInstructions() {
        this.eventSubscriber = this.eventManager.subscribe(
            'dossierInstructionListModification',
            (response) => this.load(this.dossierInstruction.id)
        );
    }
}
