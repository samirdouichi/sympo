import { BaseEntity } from './../../shared';

export class CorrespondanceType implements BaseEntity {
    constructor(
        public id?: number,
        public codeCorrespondance?: string,
        public nomCorrespondance?: string,
        public custom?: BaseEntity,
    ) {
    }
}
