import { BaseEntity } from './../../shared';

export class Debiteur implements BaseEntity {
    constructor(
        public id?: number,
        public codeDebiteur?: string,
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
        public nomDebiteur?: string,
        public cin?: string,
        public profession?: string,
        public activated?: boolean,
        public remarque?: string,
        public debiteur?: BaseEntity,
    ) {
        this.activated = false;
    }
}
