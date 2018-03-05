/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { CorrespondanceTypeComponent } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type.component';
import { CorrespondanceTypeService } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type.service';
import { CorrespondanceType } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type.model';

describe('Component Tests', () => {

    describe('CorrespondanceType Management Component', () => {
        let comp: CorrespondanceTypeComponent;
        let fixture: ComponentFixture<CorrespondanceTypeComponent>;
        let service: CorrespondanceTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CorrespondanceTypeComponent],
                providers: [
                    CorrespondanceTypeService
                ]
            })
            .overrideTemplate(CorrespondanceTypeComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorrespondanceTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorrespondanceTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new CorrespondanceType(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.correspondanceTypes[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
