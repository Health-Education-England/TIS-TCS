export class PostTisProgrammes {
	constructor(public id?: number,
				public nationalPostNumber?: string,
				public status?: string,
				public postOwner?: string,
				public mainSiteLocated?: string,
				public leadSite?: string,
				public employingBody?: string,
				public trainingBody?: string,
				public approvedGrade?: string,
				public postSpecialty?: string,
				public fullTimeEquivelent?: number,
				public leadProvider?: string,
				public oldPostId?: number,
				public newPostId?: number,) {
	}
}
