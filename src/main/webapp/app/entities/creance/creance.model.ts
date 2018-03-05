import { BaseEntity } from './../../shared';

export const enum CreancesType {
    'FACTURE',
    'CHEQUE',
    'LETTRECHANGE',
    'CONVENTION',
    'CONTRTA'
}

export class Creance implements BaseEntity {
    constructor(
        public id?: number,
        public numCreance?: string,
        public nomCreance?: string,
        public creancesType?: CreancesType,
        public createdAt?: any,
        public updatedAt?: any,
        public remarque?: string,
        public creance?: BaseEntity,
    ) {
    }
}
