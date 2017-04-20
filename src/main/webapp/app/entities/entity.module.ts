import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {TcsCurriculumTisProgrammesModule} from "./curriculum/curriculum-tis-programmes.module";
import {TcsGradeTisProgrammesModule} from "./grade/grade-tis-programmes.module";
import {TcsProgrammeTisProgrammesModule} from "./programme/programme-tis-programmes.module";
import {TcsProgrammeMembershipTisProgrammesModule} from "./programme-membership/programme-membership-tis-programmes.module";
import {TcsSpecialtyTisProgrammesModule} from "./specialty/specialty-tis-programmes.module";
import {TcsSpecialtyGroupTisProgrammesModule} from "./specialty-group/specialty-group-tis-programmes.module";
import {TcsTrainingNumberTisProgrammesModule} from "./training-number/training-number-tis-programmes.module";
import {TcsFundingTisProgrammesModule} from "./funding/funding-tis-programmes.module";
import {TcsFundingComponentsTisProgrammesModule} from "./funding-components/funding-components-tis-programmes.module";
import {TcsPlacementFunderTisProgrammesModule} from "./placement-funder/placement-funder-tis-programmes.module";
import {TcsPlacementTisProgrammesModule} from "./placement/placement-tis-programmes.module";
import {TcsPostTisProgrammesModule} from "./post/post-tis-programmes.module";
import {TcsPostFundingTisProgrammesModule} from "./post-funding/post-funding-tis-programmes.module";
import {TcsTariffFundingTypeFieldsTisProgrammesModule} from "./tariff-funding-type-fields/tariff-funding-type-fields-tis-programmes.module";
import {TcsTariffRateTisProgrammesModule} from "./tariff-rate/tariff-rate-tis-programmes.module";

/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
	imports: [
		TcsCurriculumTisProgrammesModule,
		TcsGradeTisProgrammesModule,
		TcsProgrammeTisProgrammesModule,
		TcsProgrammeMembershipTisProgrammesModule,
		TcsSpecialtyTisProgrammesModule,
		TcsSpecialtyGroupTisProgrammesModule,
		TcsTrainingNumberTisProgrammesModule,
		TcsFundingTisProgrammesModule,
		TcsFundingComponentsTisProgrammesModule,
		TcsPlacementFunderTisProgrammesModule,
		TcsPlacementTisProgrammesModule,
		TcsPostTisProgrammesModule,
		TcsPostFundingTisProgrammesModule,
		TcsTariffFundingTypeFieldsTisProgrammesModule,
		TcsTariffRateTisProgrammesModule,
		/* jhipster-needle-add-entity-module - JHipster will add entity modules here */
	],
	declarations: [],
	entryComponents: [],
	providers: [],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsEntityModule {
}
