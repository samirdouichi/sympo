import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CorrespondanceDocument } from './correspondance-document.model';
import { CorrespondanceDocumentPopupService } from './correspondance-document-popup.service';
import { CorrespondanceDocumentService } from './correspondance-document.service';

@Component({
    selector: 'jhi-correspondance-document-delete-dialog',
    templateUrl: './correspondance-document-delete-dialog.component.html'
})
export class CorrespondanceDocumentDeleteDialogComponent {

    correspondanceDocument: CorrespondanceDocument;

    constructor(
        private correspondanceDocumentService: CorrespondanceDocumentService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.correspondanceDocumentService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'correspondanceDocumentListModification',
                content: 'Deleted an correspondanceDocument'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-correspondance-document-delete-popup',
    template: ''
})
export class CorrespondanceDocumentDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private correspondanceDocumentPopupService: CorrespondanceDocumentPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.correspondanceDocumentPopupService
                .open(CorrespondanceDocumentDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
