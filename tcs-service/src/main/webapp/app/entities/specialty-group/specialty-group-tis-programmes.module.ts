import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {TcsSharedModule} from "../../shared";
import {
	SpecialtyGroupTisProgrammesService,
	SpecialtyGroupTisProgrammesPopupService,
	SpecialtyGroupTisProgrammesComponent,
	SpecialtyGroupTisProgrammesDetailComponent,
	SpecialtyGroupTisProgrammesDialogComponent,
	SpecialtyGroupTisProgrammesPopupComponent,
	SpecialtyGroupTisProgrammesDeletePopupComponent,
	SpecialtyGroupTisProgrammesDeleteDialogComponent,
	specialtyGroupRoute,
	specialtyGroupPopupRoute
} from "./";

let ENTITY_STATES = [
	...specialtyGroupRoute,
	...specialtyGroupPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		SpecialtyGroupTisProgrammesComponent,
		SpecialtyGroupTisProgrammesDetailComponent,
		SpecialtyGroupTisProgrammesDialogComponent,
		SpecialtyGroupTisProgrammesDeleteDialogComponent,
		SpecialtyGroupTisProgrammesPopupComponent,
		SpecialtyGroupTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		SpecialtyGroupTisProgrammesComponent,
		SpecialtyGroupTisProgrammesDialogComponent,
		SpecialtyGroupTisProgrammesPopupComponent,
		SpecialtyGroupTisProgrammesDeleteDialogComponent,
		SpecialtyGroupTisProgrammesDeletePopupComponent,
	],
	providers: [
		SpecialtyGroupTisProgrammesService,
		SpecialtyGroupTisProgrammesPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsSpecialtyGroupTisProgrammesModule {
}
