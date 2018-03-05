import { BaseEntity } from './../../shared';

export class DossierInstruction implements BaseEntity {
    constructor(
        public id?: number,
        public numInstruction?: string,
        public instruction?: string,
        public supportInstruction?: string,
        public dateCreationInstruction?: any,
        public remarque?: string,
        public custom?: BaseEntity,
        public dossierInstruction?: BaseEntity,
    ) {
    }
}
