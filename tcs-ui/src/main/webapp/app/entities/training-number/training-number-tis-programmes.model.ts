const enum TrainingNumberType {
	'NTN',
	'DRN'

}
;
export class TrainingNumberTisProgrammes {
	constructor(public id ?: number,
				public trainingNumberType ?: TrainingNumberType,
				public localOffice ?: string,
				public number ?: number,
				public appointmentYear ?: number,
				public typeOfContract ?: string,
				public suffix ?: string,
				public programmeMembershipId ?: number) {
	}
}
