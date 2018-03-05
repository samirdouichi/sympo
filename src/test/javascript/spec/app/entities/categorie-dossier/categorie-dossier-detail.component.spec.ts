/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SympoTestModule } from '../../../test.module';
import { CategorieDossierDetailComponent } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier-detail.component';
import { CategorieDossierService } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier.service';
import { CategorieDossier } from '../../../../../../main/webapp/app/entities/categorie-dossier/categorie-dossier.model';

describe('Component Tests', () => {

    describe('CategorieDossier Management Detail Component', () => {
        let comp: CategorieDossierDetailComponent;
        let fixture: ComponentFixture<CategorieDossierDetailComponent>;
        let service: CategorieDossierService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SympoTestModule],
                declarations: [CategorieDossierDetailComponent],
                providers: [
                    CategorieDossierService
                ]
            })
            .overrideTemplate(CategorieDossierDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CategorieDossierDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CategorieDossierService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new CategorieDossier(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.categorieDossier).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
