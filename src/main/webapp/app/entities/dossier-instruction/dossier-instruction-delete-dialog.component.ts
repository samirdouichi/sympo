import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { DossierInstruction } from './dossier-instruction.model';
import { DossierInstructionPopupService } from './dossier-instruction-popup.service';
import { DossierInstructionService } from './dossier-instruction.service';

@Component({
    selector: 'jhi-dossier-instruction-delete-dialog',
    templateUrl: './dossier-instruction-delete-dialog.component.html'
})
export class DossierInstructionDeleteDialogComponent {

    dossierInstruction: DossierInstruction;

    constructor(
        private dossierInstructionService: DossierInstructionService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dossierInstructionService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'dossierInstructionListModification',
                content: 'Deleted an dossierInstruction'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-dossier-instruction-delete-popup',
    template: ''
})
export class DossierInstructionDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dossierInstructionPopupService: DossierInstructionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.dossierInstructionPopupService
                .open(DossierInstructionDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
