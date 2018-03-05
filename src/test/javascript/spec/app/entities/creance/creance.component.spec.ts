/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { CreanceComponent } from '../../../../../../main/webapp/app/entities/creance/creance.component';
import { CreanceService } from '../../../../../../main/webapp/app/entities/creance/creance.service';
import { Creance } from '../../../../../../main/webapp/app/entities/creance/creance.model';

describe('Component Tests', () => {

    describe('Creance Management Component', () => {
        let comp: CreanceComponent;
        let fixture: ComponentFixture<CreanceComponent>;
        let service: CreanceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CreanceComponent],
                providers: [
                    CreanceService
                ]
            })
            .overrideTemplate(CreanceComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CreanceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CreanceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Creance(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.creances[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
