/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { CabinetComponent } from '../../../../../../main/webapp/app/entities/cabinet/cabinet.component';
import { CabinetService } from '../../../../../../main/webapp/app/entities/cabinet/cabinet.service';
import { Cabinet } from '../../../../../../main/webapp/app/entities/cabinet/cabinet.model';

describe('Component Tests', () => {

    describe('Cabinet Management Component', () => {
        let comp: CabinetComponent;
        let fixture: ComponentFixture<CabinetComponent>;
        let service: CabinetService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CabinetComponent],
                providers: [
                    CabinetService
                ]
            })
            .overrideTemplate(CabinetComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CabinetComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CabinetService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Cabinet(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.cabinets[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
