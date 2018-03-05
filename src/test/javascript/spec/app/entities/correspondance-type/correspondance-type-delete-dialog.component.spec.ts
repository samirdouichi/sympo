/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SympoTestModule } from '../../../test.module';
import { CorrespondanceTypeDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type-delete-dialog.component';
import { CorrespondanceTypeService } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type.service';

describe('Component Tests', () => {

    describe('CorrespondanceType Management Delete Component', () => {
        let comp: CorrespondanceTypeDeleteDialogComponent;
        let fixture: ComponentFixture<CorrespondanceTypeDeleteDialogComponent>;
        let service: CorrespondanceTypeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CorrespondanceTypeDeleteDialogComponent],
                providers: [
                    CorrespondanceTypeService
                ]
            })
            .overrideTemplate(CorrespondanceTypeDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorrespondanceTypeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorrespondanceTypeService);
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
