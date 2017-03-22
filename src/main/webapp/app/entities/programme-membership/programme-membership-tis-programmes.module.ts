import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {TcsSharedModule} from "../../shared";
import {
	ProgrammeMembershipTisProgrammesService,
	ProgrammeMembershipTisProgrammesPopupService,
	ProgrammeMembershipTisProgrammesComponent,
	ProgrammeMembershipTisProgrammesDetailComponent,
	ProgrammeMembershipTisProgrammesDialogComponent,
	ProgrammeMembershipTisProgrammesPopupComponent,
	ProgrammeMembershipTisProgrammesDeletePopupComponent,
	ProgrammeMembershipTisProgrammesDeleteDialogComponent,
	programmeMembershipRoute,
	programmeMembershipPopupRoute,
	ProgrammeMembershipTisProgrammesResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...programmeMembershipRoute,
	...programmeMembershipPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		ProgrammeMembershipTisProgrammesComponent,
		ProgrammeMembershipTisProgrammesDetailComponent,
		ProgrammeMembershipTisProgrammesDialogComponent,
		ProgrammeMembershipTisProgrammesDeleteDialogComponent,
		ProgrammeMembershipTisProgrammesPopupComponent,
		ProgrammeMembershipTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		ProgrammeMembershipTisProgrammesComponent,
		ProgrammeMembershipTisProgrammesDialogComponent,
		ProgrammeMembershipTisProgrammesPopupComponent,
		ProgrammeMembershipTisProgrammesDeleteDialogComponent,
		ProgrammeMembershipTisProgrammesDeletePopupComponent,
	],
	providers: [
		ProgrammeMembershipTisProgrammesService,
		ProgrammeMembershipTisProgrammesPopupService,
		ProgrammeMembershipTisProgrammesResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsProgrammeMembershipTisProgrammesModule {
}
