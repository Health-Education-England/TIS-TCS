import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {TcsCurriculumTisProgrammesModule} from "./curriculum/curriculum-tis-programmes.module";
import {TcsGradeTisProgrammesModule} from "./grade/grade-tis-programmes.module";
import {TcsProgrammeTisProgrammesModule} from "./programme/programme-tis-programmes.module";
import {TcsProgrammeMembershipTisProgrammesModule} from "./programme-membership/programme-membership-tis-programmes.module";
import {TcsSpecialtyTisProgrammesModule} from "./specialty/specialty-tis-programmes.module";
import {TcsSpecialtyGroupTisProgrammesModule} from "./specialty-group/specialty-group-tis-programmes.module";
import {TcsTrainingNumberTisProgrammesModule} from "./training-number/training-number-tis-programmes.module";
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
		/* jhipster-needle-add-entity-module - JHipster will add entity modules here */
	],
	declarations: [],
	entryComponents: [],
	providers: [],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsEntityModule {
}
