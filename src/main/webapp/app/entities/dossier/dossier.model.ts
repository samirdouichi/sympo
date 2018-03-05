import { BaseEntity } from './../../shared';

export const enum EtatDossier {
    'ENCOURS',
    'ANNULE',
    'ARRETE',
    'SUSPENDU'
}

export class Dossier implements BaseEntity {
    constructor(
        public id?: number,
        public numDossier?: string,
        public status?: EtatDossier,
        public nomDossier?: string,
        public dateCreation?: any,
        public updatedAt?: any,
        public dateCloture?: any,
        public remarque?: string,
        public dossierCategories?: BaseEntity[],
        public dossierDebiteurs?: BaseEntity[],
        public dossierPartenaires?: BaseEntity[],
        public dossierCorrespondances?: BaseEntity[],
        public dossierInstructions?: BaseEntity[],
        public dossier?: BaseEntity,
    ) {
    }
}
