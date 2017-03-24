const enum CurriculumSubType {
	'MEDICAL_CURRICULUM',
	'MEDICAL_SPR',
	'DENTAL_CURRICULUM',
	'DENTAL_SPR',
	'SUB_SPECIALTY',
	'DENTAL_POST_CCST',
	'ACF_OTHER_FUNDING',
	'ACL',
	'AFT',
	'ACL_OTHER_FUNDING',
	'CLINICAL_LECTURER',
	'CLINICAL_TEACHING_FELLOW',
	'CLINICAL_RESEARCH_FELLOW',
	'ACFNIHR_FUNDING',
	'ACLNIHR_FUNDING',
	'OTHER_FELLOWSHIP'

}
;

const enum AssessmentType {
	'ARCP',
	'RITA',
	'ACADEMIC'

}
;
export class CurriculumTisProgrammes {
	constructor(public id ?: number,
				public name ?: string,
				public start ?: any,
				public end ?: any,
				public curriculumSubType ?: CurriculumSubType,
				public assessmentType ?: AssessmentType,
				public doesThisCurriculumLeadToCct ?: boolean,
				public periodOfGrace ?: number,
				public programmeMembershipId ?: number,
				public gradeId ?: number,
				public specialtyId ?: number) {
		this.doesThisCurriculumLeadToCct = false;
	}
}
