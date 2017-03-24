const enum Status {
	'CURRENT',
	'INACTIVE',
	'DELETE'

}
;
export class ProgrammeTisProgrammes {
	constructor(public id ?: number,
				public status ?: Status,
				public managingDeanery ?: string,
				public programmeName ?: string,
				public programmeNumber ?: string,
				public leadProvider ?: string,
				public programmeMembershipId ?: number) {
	}
}
