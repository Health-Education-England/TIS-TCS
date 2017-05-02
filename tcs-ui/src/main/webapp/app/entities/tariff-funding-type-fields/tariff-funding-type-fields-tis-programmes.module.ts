import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TcsSharedModule} from '../../shared';
import {
	TariffFundingTypeFieldsTisProgrammesService,
	TariffFundingTypeFieldsTisProgrammesPopupService,
	TariffFundingTypeFieldsTisProgrammesComponent,
	TariffFundingTypeFieldsTisProgrammesDetailComponent,
	TariffFundingTypeFieldsTisProgrammesDialogComponent,
	TariffFundingTypeFieldsTisProgrammesPopupComponent,
	TariffFundingTypeFieldsTisProgrammesDeletePopupComponent,
	TariffFundingTypeFieldsTisProgrammesDeleteDialogComponent,
	tariffFundingTypeFieldsRoute,
	tariffFundingTypeFieldsPopupRoute,
} from './';

const ENTITY_STATES = [
	...tariffFundingTypeFieldsRoute,
	...tariffFundingTypeFieldsPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		TariffFundingTypeFieldsTisProgrammesComponent,
		TariffFundingTypeFieldsTisProgrammesDetailComponent,
		TariffFundingTypeFieldsTisProgrammesDialogComponent,
		TariffFundingTypeFieldsTisProgrammesDeleteDialogComponent,
		TariffFundingTypeFieldsTisProgrammesPopupComponent,
		TariffFundingTypeFieldsTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		TariffFundingTypeFieldsTisProgrammesComponent,
		TariffFundingTypeFieldsTisProgrammesDialogComponent,
		TariffFundingTypeFieldsTisProgrammesPopupComponent,
		TariffFundingTypeFieldsTisProgrammesDeleteDialogComponent,
		TariffFundingTypeFieldsTisProgrammesDeletePopupComponent,
	],
	providers: [
		TariffFundingTypeFieldsTisProgrammesService,
		TariffFundingTypeFieldsTisProgrammesPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsTariffFundingTypeFieldsTisProgrammesModule {
}
