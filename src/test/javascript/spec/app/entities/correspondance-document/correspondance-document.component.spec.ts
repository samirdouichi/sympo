/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { CorrespondanceDocumentComponent } from '../../../../../../main/webapp/app/entities/correspondance-document/correspondance-document.component';
import { CorrespondanceDocumentService } from '../../../../../../main/webapp/app/entities/correspondance-document/correspondance-document.service';
import { CorrespondanceDocument } from '../../../../../../main/webapp/app/entities/correspondance-document/correspondance-document.model';

describe('Component Tests', () => {

    describe('CorrespondanceDocument Management Component', () => {
        let comp: CorrespondanceDocumentComponent;
        let fixture: ComponentFixture<CorrespondanceDocumentComponent>;
        let service: CorrespondanceDocumentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CorrespondanceDocumentComponent],
                providers: [
                    CorrespondanceDocumentService
                ]
            })
            .overrideTemplate(CorrespondanceDocumentComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorrespondanceDocumentComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorrespondanceDocumentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CorrespondanceDocument(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.correspondanceDocuments[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
