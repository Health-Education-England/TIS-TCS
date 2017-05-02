const enum PlacementType {
	'INPOSTSTANDARD',
	'INPOSTEXTENSION',
	'OOPC',
	'OOPE',
	'OOPR',
	'OOPT',
	'PARENTALLEAVE',
	'SICKLEAVE'

}
;
export class PlacementTisProgrammes {
	constructor(public id?: number,
				public status?: string,
				public nationalPostNumber?: string,
				public site?: string,
				public grade?: string,
				public specialty?: string,
				public dateFrom?: any,
				public dateTo?: any,
				public placementType?: PlacementType,
				public placementWholeTimeEquivalent?: number,
				public slotShare?: boolean,) {
		this.slotShare = false;
	}
}
