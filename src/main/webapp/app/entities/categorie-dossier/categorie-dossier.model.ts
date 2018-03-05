import { BaseEntity } from './../../shared';

export class CategorieDossier implements BaseEntity {
    constructor(
        public id?: number,
        public numCatDossier?: string,
        public nomCatDossier?: string,
        public remarque?: string,
        public categorieDossier?: BaseEntity,
    ) {
    }
}
