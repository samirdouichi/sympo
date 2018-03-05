import { BaseEntity } from './../../shared';

export const enum EtatCorrespondance {
    'MED',
    'MAKAL'
}

export class Correspondance implements BaseEntity {
    constructor(
        public id?: number,
        public correspondanceNom?: string,
        public etatCorrespondance?: EtatCorrespondance,
        public remarque?: string,
        public createdAt?: any,
        public updatedAt?: any,
        public correspondanceTypes?: BaseEntity,
        public correspondanceDocuments?: BaseEntity,
        public correspondance?: BaseEntity,
    ) {
    }
}
