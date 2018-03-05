/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { CorrespondanceDetailComponent } from '../../../../../../main/webapp/app/entities/correspondance/correspondance-detail.component';
import { CorrespondanceService } from '../../../../../../main/webapp/app/entities/correspondance/correspondance.service';
import { Correspondance } from '../../../../../../main/webapp/app/entities/correspondance/correspondance.model';

describe('Component Tests', () => {

    describe('Correspondance Management Detail Component', () => {
        let comp: CorrespondanceDetailComponent;
        let fixture: ComponentFixture<CorrespondanceDetailComponent>;
        let service: CorrespondanceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CorrespondanceDetailComponent],
                providers: [
                    CorrespondanceService
                ]
            })
            .overrideTemplate(CorrespondanceDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorrespondanceDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorrespondanceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Correspondance(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.correspondance).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
