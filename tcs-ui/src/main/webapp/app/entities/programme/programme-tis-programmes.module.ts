import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {TcsSharedModule} from "../../shared";
import {
	ProgrammeTisProgrammesService,
	ProgrammeTisProgrammesPopupService,
	ProgrammeTisProgrammesComponent,
	ProgrammeTisProgrammesDetailComponent,
	ProgrammeTisProgrammesDialogComponent,
	ProgrammeTisProgrammesPopupComponent,
	ProgrammeTisProgrammesDeletePopupComponent,
	ProgrammeTisProgrammesDeleteDialogComponent,
	programmeRoute,
	programmePopupRoute,
	ProgrammeTisProgrammesResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...programmeRoute,
	...programmePopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		ProgrammeTisProgrammesComponent,
		ProgrammeTisProgrammesDetailComponent,
		ProgrammeTisProgrammesDialogComponent,
		ProgrammeTisProgrammesDeleteDialogComponent,
		ProgrammeTisProgrammesPopupComponent,
		ProgrammeTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		ProgrammeTisProgrammesComponent,
		ProgrammeTisProgrammesDialogComponent,
		ProgrammeTisProgrammesPopupComponent,
		ProgrammeTisProgrammesDeleteDialogComponent,
		ProgrammeTisProgrammesDeletePopupComponent,
	],
	providers: [
		ProgrammeTisProgrammesService,
		ProgrammeTisProgrammesPopupService,
		ProgrammeTisProgrammesResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsProgrammeTisProgrammesModule {
}
