import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {TcsSharedModule} from "../../shared";
import {
	SpecialtyTisProgrammesService,
	SpecialtyTisProgrammesPopupService,
	SpecialtyTisProgrammesComponent,
	SpecialtyTisProgrammesDetailComponent,
	SpecialtyTisProgrammesDialogComponent,
	SpecialtyTisProgrammesPopupComponent,
	SpecialtyTisProgrammesDeletePopupComponent,
	SpecialtyTisProgrammesDeleteDialogComponent,
	specialtyRoute,
	specialtyPopupRoute,
	SpecialtyTisProgrammesResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...specialtyRoute,
	...specialtyPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		SpecialtyTisProgrammesComponent,
		SpecialtyTisProgrammesDetailComponent,
		SpecialtyTisProgrammesDialogComponent,
		SpecialtyTisProgrammesDeleteDialogComponent,
		SpecialtyTisProgrammesPopupComponent,
		SpecialtyTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		SpecialtyTisProgrammesComponent,
		SpecialtyTisProgrammesDialogComponent,
		SpecialtyTisProgrammesPopupComponent,
		SpecialtyTisProgrammesDeleteDialogComponent,
		SpecialtyTisProgrammesDeletePopupComponent,
	],
	providers: [
		SpecialtyTisProgrammesService,
		SpecialtyTisProgrammesPopupService,
		SpecialtyTisProgrammesResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsSpecialtyTisProgrammesModule {
}
