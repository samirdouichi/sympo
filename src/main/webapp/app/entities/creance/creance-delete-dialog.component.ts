import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Creance } from './creance.model';
import { CreancePopupService } from './creance-popup.service';
import { CreanceService } from './creance.service';

@Component({
    selector: 'jhi-creance-delete-dialog',
    templateUrl: './creance-delete-dialog.component.html'
})
export class CreanceDeleteDialogComponent {

    creance: Creance;

    constructor(
        private creanceService: CreanceService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.creanceService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'creanceListModification',
                content: 'Deleted an creance'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-creance-delete-popup',
    template: ''
})
export class CreanceDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private creancePopupService: CreancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.creancePopupService
                .open(CreanceDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
