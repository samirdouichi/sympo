import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Partenaire } from './partenaire.model';
import { PartenaireService } from './partenaire.service';

@Injectable()
export class PartenairePopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private partenaireService: PartenaireService

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
                this.partenaireService.find(id)
                    .subscribe((partenaireResponse: HttpResponse<Partenaire>) => {
                        const partenaire: Partenaire = partenaireResponse.body;
                        partenaire.resetDate = this.datePipe
                            .transform(partenaire.resetDate, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.partenaireModalRef(component, partenaire);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.partenaireModalRef(component, new Partenaire());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    partenaireModalRef(component: Component, partenaire: Partenaire): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.partenaire = partenaire;
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
