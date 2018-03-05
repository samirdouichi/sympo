/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { CabinetDetailComponent } from '../../../../../../main/webapp/app/entities/cabinet/cabinet-detail.component';
import { CabinetService } from '../../../../../../main/webapp/app/entities/cabinet/cabinet.service';
import { Cabinet } from '../../../../../../main/webapp/app/entities/cabinet/cabinet.model';

describe('Component Tests', () => {

    describe('Cabinet Management Detail Component', () => {
        let comp: CabinetDetailComponent;
        let fixture: ComponentFixture<CabinetDetailComponent>;
        let service: CabinetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CabinetDetailComponent],
                providers: [
                    CabinetService
                ]
            })
            .overrideTemplate(CabinetDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CabinetDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CabinetService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Cabinet(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.cabinet).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
