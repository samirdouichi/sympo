/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { CorrespondanceTypeDetailComponent } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type-detail.component';
import { CorrespondanceTypeService } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type.service';
import { CorrespondanceType } from '../../../../../../main/webapp/app/entities/correspondance-type/correspondance-type.model';

describe('Component Tests', () => {

    describe('CorrespondanceType Management Detail Component', () => {
        let comp: CorrespondanceTypeDetailComponent;
        let fixture: ComponentFixture<CorrespondanceTypeDetailComponent>;
        let service: CorrespondanceTypeService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CorrespondanceTypeDetailComponent],
                providers: [
                    CorrespondanceTypeService
                ]
            })
            .overrideTemplate(CorrespondanceTypeDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorrespondanceTypeDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorrespondanceTypeService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CorrespondanceType(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.correspondanceType).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
