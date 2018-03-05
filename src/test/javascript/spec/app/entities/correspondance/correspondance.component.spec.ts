/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { SympoTestModule } from '../../../test.module';
import { CorrespondanceComponent } from '../../../../../../main/webapp/app/entities/correspondance/correspondance.component';
import { CorrespondanceService } from '../../../../../../main/webapp/app/entities/correspondance/correspondance.service';
import { Correspondance } from '../../../../../../main/webapp/app/entities/correspondance/correspondance.model';

describe('Component Tests', () => {

    describe('Correspondance Management Component', () => {
        let comp: CorrespondanceComponent;
        let fixture: ComponentFixture<CorrespondanceComponent>;
        let service: CorrespondanceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CorrespondanceComponent],
                providers: [
                    CorrespondanceService
                ]
            })
            .overrideTemplate(CorrespondanceComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CorrespondanceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CorrespondanceService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Correspondance(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.correspondances[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
