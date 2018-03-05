/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { CreanceDetailComponent } from '../../../../../../main/webapp/app/entities/creance/creance-detail.component';
import { CreanceService } from '../../../../../../main/webapp/app/entities/creance/creance.service';
import { Creance } from '../../../../../../main/webapp/app/entities/creance/creance.model';

describe('Component Tests', () => {

    describe('Creance Management Detail Component', () => {
        let comp: CreanceDetailComponent;
        let fixture: ComponentFixture<CreanceDetailComponent>;
        let service: CreanceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CreanceDetailComponent],
                providers: [
                    CreanceService
                ]
            })
            .overrideTemplate(CreanceDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CreanceDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CreanceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Creance(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.creance).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
