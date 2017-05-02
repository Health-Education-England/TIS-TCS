const enum FundingType {
	'TARIFF',
	'MADEL',
	'TRUST',
	'OTHER'

};
export class FundingTisProgrammes {
	constructor(public id?: number,
				public status?: string,
				public startDate?: any,
				public endDate?: any,
				public fundingType?: FundingType,
				public fundingIssue?: string) {
	}
}
