import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {TcsSharedModule} from "../../shared";
import {
	CurriculumTisProgrammesService,
	CurriculumTisProgrammesPopupService,
	CurriculumTisProgrammesComponent,
	CurriculumTisProgrammesDetailComponent,
	CurriculumTisProgrammesDialogComponent,
	CurriculumTisProgrammesPopupComponent,
	CurriculumTisProgrammesDeletePopupComponent,
	CurriculumTisProgrammesDeleteDialogComponent,
	curriculumRoute,
	curriculumPopupRoute,
	CurriculumTisProgrammesResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...curriculumRoute,
	...curriculumPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		CurriculumTisProgrammesComponent,
		CurriculumTisProgrammesDetailComponent,
		CurriculumTisProgrammesDialogComponent,
		CurriculumTisProgrammesDeleteDialogComponent,
		CurriculumTisProgrammesPopupComponent,
		CurriculumTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		CurriculumTisProgrammesComponent,
		CurriculumTisProgrammesDialogComponent,
		CurriculumTisProgrammesPopupComponent,
		CurriculumTisProgrammesDeleteDialogComponent,
		CurriculumTisProgrammesDeletePopupComponent,
	],
	providers: [
		CurriculumTisProgrammesService,
		CurriculumTisProgrammesPopupService,
		CurriculumTisProgrammesResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsCurriculumTisProgrammesModule {
}
