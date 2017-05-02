const enum ProgrammeMembershipType {
	'SUBSTANTIVE',
	'LAT',
	'FTSTA',
	'MILITARY',
	'VISITOR'

}
;
export class ProgrammeMembershipTisProgrammes {
	constructor(public id ?: number,
				public programmeMembershipType ?: ProgrammeMembershipType,
				public rotation ?: string,
				public curriculumStartDate ?: any,
				public curriculumEndDate ?: any,
				public periodOfGrace ?: number,
				public programmeStartDate ?: any,
				public curriculumCompletionDate ?: any,
				public programmeEndDate ?: any,
				public leavingDestination ?: string,
				public programmeId ?: number,
				public curriculumId ?: number,
				public trainingNumberId ?: number) {
	}
}
