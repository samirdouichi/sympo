import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CorrespondanceDocument } from './correspondance-document.model';
import { CorrespondanceDocumentService } from './correspondance-document.service';

@Injectable()
export class CorrespondanceDocumentPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private correspondanceDocumentService: CorrespondanceDocumentService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.correspondanceDocumentService.find(id)
                    .subscribe((correspondanceDocumentResponse: HttpResponse<CorrespondanceDocument>) => {
                        const correspondanceDocument: CorrespondanceDocument = correspondanceDocumentResponse.body;
                        this.ngbModalRef = this.correspondanceDocumentModalRef(component, correspondanceDocument);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.correspondanceDocumentModalRef(component, new CorrespondanceDocument());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    correspondanceDocumentModalRef(component: Component, correspondanceDocument: CorrespondanceDocument): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.correspondanceDocument = correspondanceDocument;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
