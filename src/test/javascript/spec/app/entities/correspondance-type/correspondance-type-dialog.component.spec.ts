/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SympoTestModule } from '../../../test.module';
import { CorrespondanceTypeDialogComponent } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type-dialog.component';
import { CorrespondanceTypeService } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type.service';
import { CorrespondanceType } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type.model';
import { CorrespondanceService } from '../../../../../../main/webapp/app/entities/correspondance';

describe('Component Tests', () => {

    describe('CorrespondanceType Management Dialog Component', () => {
        let comp: CorrespondanceTypeDialogComponent;
        let fixture: ComponentFixture<CorrespondanceTypeDialogComponent>;
        let service: CorrespondanceTypeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CorrespondanceTypeDialogComponent],
                providers: [
                    CorrespondanceService,
                    CorrespondanceTypeService
                ]
            })
            .overrideTemplate(CorrespondanceTypeDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorrespondanceTypeDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorrespondanceTypeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CorrespondanceType(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.correspondanceType = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'correspondanceTypeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new CorrespondanceType();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.correspondanceType = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'correspondanceTypeListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
