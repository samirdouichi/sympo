import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DossierInstruction } from './dossier-instruction.model';
import { DossierInstructionService } from './dossier-instruction.service';

@Injectable()
export class DossierInstructionPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private dossierInstructionService: DossierInstructionService

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
                this.dossierInstructionService.find(id)
                    .subscribe((dossierInstructionResponse: HttpResponse<DossierInstruction>) => {
                        const dossierInstruction: DossierInstruction = dossierInstructionResponse.body;
                        if (dossierInstruction.dateCreationInstruction) {
                            dossierInstruction.dateCreationInstruction = {
                                year: dossierInstruction.dateCreationInstruction.getFullYear(),
                                month: dossierInstruction.dateCreationInstruction.getMonth() + 1,
                                day: dossierInstruction.dateCreationInstruction.getDate()
                            };
                        }
                        this.ngbModalRef = this.dossierInstructionModalRef(component, dossierInstruction);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.dossierInstructionModalRef(component, new DossierInstruction());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    dossierInstructionModalRef(component: Component, dossierInstruction: DossierInstruction): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.dossierInstruction = dossierInstruction;
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
