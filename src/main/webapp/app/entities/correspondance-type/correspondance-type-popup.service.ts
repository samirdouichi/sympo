import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { CorrespondanceType } from './correspondance-type.model';
import { CorrespondanceTypeService } from './correspondance-type.service';

@Injectable()
export class CorrespondanceTypePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private correspondanceTypeService: CorrespondanceTypeService

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
                this.correspondanceTypeService.find(id)
                    .subscribe((correspondanceTypeResponse: HttpResponse<CorrespondanceType>) => {
                        const correspondanceType: CorrespondanceType = correspondanceTypeResponse.body;
                        this.ngbModalRef = this.correspondanceTypeModalRef(component, correspondanceType);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.correspondanceTypeModalRef(component, new CorrespondanceType());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    correspondanceTypeModalRef(component: Component, correspondanceType: CorrespondanceType): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.correspondanceType = correspondanceType;
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
