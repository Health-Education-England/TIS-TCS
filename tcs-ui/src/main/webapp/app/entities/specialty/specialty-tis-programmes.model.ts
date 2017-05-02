const enum Status {
	'CURRENT',
	'INACTIVE',
	'DELETE'

}
;

const enum SpecialtyType {
	'CURRICULUM',
	'POST',
	'PLACEMENT',
	'SUB_SPECIALTY'

}
;
export class SpecialtyTisProgrammes {
	constructor(public id ?: number,
				public status ?: Status,
				public college ?: string,
				public nhsSpecialtyCode ?: string,
				public specialtyType ?: SpecialtyType,
				public curriculumId ?: number,
				public specialtyGroupId ?: number) {
	}
}
