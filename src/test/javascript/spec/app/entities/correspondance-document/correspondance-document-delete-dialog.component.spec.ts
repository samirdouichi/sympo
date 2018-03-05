/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SympoTestModule } from '../../../test.module';
import { CorrespondanceDocumentDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/correspondance-document/correspondance-document-delete-dialog.component';
import { CorrespondanceDocumentService } from '../../../../../../main/webapp/app/entities/correspondance-document/correspondance-document.service';

describe('Component Tests', () => {

    describe('CorrespondanceDocument Management Delete Component', () => {
        let comp: CorrespondanceDocumentDeleteDialogComponent;
        let fixture: ComponentFixture<CorrespondanceDocumentDeleteDialogComponent>;
        let service: CorrespondanceDocumentService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CorrespondanceDocumentDeleteDialogComponent],
                providers: [
                    CorrespondanceDocumentService
                ]
            })
            .overrideTemplate(CorrespondanceDocumentDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorrespondanceDocumentDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorrespondanceDocumentService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
