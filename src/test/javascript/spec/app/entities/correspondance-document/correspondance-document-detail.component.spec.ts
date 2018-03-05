/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { CorrespondanceDocumentDetailComponent } from '../../../../../../main/webapp/app/entities/correspondance-document/correspondance-document-detail.component';
import { CorrespondanceDocumentService } from '../../../../../../main/webapp/app/entities/correspondance-document/correspondance-document.service';
import { CorrespondanceDocument } from '../../../../../../main/webapp/app/entities/correspondance-document/correspondance-document.model';

describe('Component Tests', () => {

    describe('CorrespondanceDocument Management Detail Component', () => {
        let comp: CorrespondanceDocumentDetailComponent;
        let fixture: ComponentFixture<CorrespondanceDocumentDetailComponent>;
        let service: CorrespondanceDocumentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CorrespondanceDocumentDetailComponent],
                providers: [
                    CorrespondanceDocumentService
                ]
            })
            .overrideTemplate(CorrespondanceDocumentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorrespondanceDocumentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorrespondanceDocumentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CorrespondanceDocument(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.correspondanceDocument).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
