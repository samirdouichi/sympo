import { BaseEntity } from './../../shared';

export class Crediteur implements BaseEntity {
    constructor(
        public id?: number,
        public codeCrediteur?: string,
        public raisonSocial?: string,
        public raisonSocialArabe?: string,
        public rc?: string,
        public patente?: string,
        public numTelephone?: string,
        public email?: string,
        public adresse?: string,
        public quartier?: string,
        public ville?: string,
        public fax?: string,
        public nomCrediteur?: string,
        public prenomCrediteur?: string,
        public activated?: boolean,
        public remarque?: string,
        public crediteur?: BaseEntity,
        public crediteurCreances?: BaseEntity[],
    ) {
        this.activated = false;
    }
}
