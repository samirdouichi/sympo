import { BaseEntity } from './../../shared';

export class CorrespondanceDocument implements BaseEntity {
    constructor(
        public id?: number,
        public correspondanceDocument?: any,
        public remarque?: string,
        public custom?: BaseEntity,
    ) {
    }
}
