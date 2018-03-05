import { BaseEntity } from './../../shared';

export class Cabinet implements BaseEntity {
    constructor(
        public id?: number,
        public raisonSocial?: string,
        public raisonSocialArabe?: string,
        public rc?: string,
        public patente?: string,
        public numTelephone?: string,
        public email?: string,
        public adresse?: string,
        public nomGerant?: string,
        public activated?: boolean,
        public dateCreation?: any,
        public remarque?: string,
        public cabinets?: BaseEntity[],
    ) {
        this.activated = false;
    }
}
