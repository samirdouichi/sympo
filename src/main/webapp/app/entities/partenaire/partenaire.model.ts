import { BaseEntity } from './../../shared';

export class Partenaire implements BaseEntity {
    constructor(
        public id?: number,
        public nomPartenaire?: string,
        public resetDate?: any,
        public partenaire?: BaseEntity,
    ) {
    }
}
