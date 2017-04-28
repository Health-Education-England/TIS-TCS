import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TcsSharedModule} from '../../shared';
import {
	TariffRateTisProgrammesService,
	TariffRateTisProgrammesPopupService,
	TariffRateTisProgrammesComponent,
	TariffRateTisProgrammesDetailComponent,
	TariffRateTisProgrammesDialogComponent,
	TariffRateTisProgrammesPopupComponent,
	TariffRateTisProgrammesDeletePopupComponent,
	TariffRateTisProgrammesDeleteDialogComponent,
	tariffRateRoute,
	tariffRatePopupRoute,
} from './';

const ENTITY_STATES = [
	...tariffRateRoute,
	...tariffRatePopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		TariffRateTisProgrammesComponent,
		TariffRateTisProgrammesDetailComponent,
		TariffRateTisProgrammesDialogComponent,
		TariffRateTisProgrammesDeleteDialogComponent,
		TariffRateTisProgrammesPopupComponent,
		TariffRateTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		TariffRateTisProgrammesComponent,
		TariffRateTisProgrammesDialogComponent,
		TariffRateTisProgrammesPopupComponent,
		TariffRateTisProgrammesDeleteDialogComponent,
		TariffRateTisProgrammesDeletePopupComponent,
	],
	providers: [
		TariffRateTisProgrammesService,
		TariffRateTisProgrammesPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsTariffRateTisProgrammesModule {
}
