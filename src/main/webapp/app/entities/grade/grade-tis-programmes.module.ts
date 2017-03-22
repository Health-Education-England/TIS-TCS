import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {TcsSharedModule} from "../../shared";
import {
	GradeTisProgrammesService,
	GradeTisProgrammesPopupService,
	GradeTisProgrammesComponent,
	GradeTisProgrammesDetailComponent,
	GradeTisProgrammesDialogComponent,
	GradeTisProgrammesPopupComponent,
	GradeTisProgrammesDeletePopupComponent,
	GradeTisProgrammesDeleteDialogComponent,
	gradeRoute,
	gradePopupRoute
} from "./";

let ENTITY_STATES = [
	...gradeRoute,
	...gradePopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		GradeTisProgrammesComponent,
		GradeTisProgrammesDetailComponent,
		GradeTisProgrammesDialogComponent,
		GradeTisProgrammesDeleteDialogComponent,
		GradeTisProgrammesPopupComponent,
		GradeTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		GradeTisProgrammesComponent,
		GradeTisProgrammesDialogComponent,
		GradeTisProgrammesPopupComponent,
		GradeTisProgrammesDeleteDialogComponent,
		GradeTisProgrammesDeletePopupComponent,
	],
	providers: [
		GradeTisProgrammesService,
		GradeTisProgrammesPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsGradeTisProgrammesModule {
}
