import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SympoCabinetModule } from './cabinet/cabinet.module';
import { SympoDossierModule } from './dossier/dossier.module';
import { SympoDossierInstructionModule } from './dossier-instruction/dossier-instruction.module';
import { SympoCategorieDossierModule } from './categorie-dossier/categorie-dossier.module';
import { SympoCorrespondanceModule } from './correspondance/correspondance.module';
import { SympoCorrespondanceDocumentModule } from './correspondance-document/correspondance-document.module';
import { SympoCorrespondanceTypeModule } from './correspondance-type/correspondance-type.module';
import { SympoDebiteurModule } from './debiteur/debiteur.module';
import { SympoCrediteurModule } from './crediteur/crediteur.module';
import { SympoCreanceModule } from './creance/creance.module';
import { SympoPartenaireModule } from './partenaire/partenaire.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SympoCabinetModule,
        SympoDossierModule,
        SympoDossierInstructionModule,
        SympoCategorieDossierModule,
        SympoCorrespondanceModule,
        SympoCorrespondanceDocumentModule,
        SympoCorrespondanceTypeModule,
        SympoDebiteurModule,
        SympoCrediteurModule,
        SympoCreanceModule,
        SympoPartenaireModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SympoEntityModule {}
