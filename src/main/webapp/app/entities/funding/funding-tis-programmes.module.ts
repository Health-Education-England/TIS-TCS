import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TcsSharedModule} from '../../shared';
import {
	FundingTisProgrammesService,
	FundingTisProgrammesPopupService,
	FundingTisProgrammesComponent,
	FundingTisProgrammesDetailComponent,
	FundingTisProgrammesDialogComponent,
	FundingTisProgrammesPopupComponent,
	FundingTisProgrammesDeletePopupComponent,
	FundingTisProgrammesDeleteDialogComponent,
	fundingRoute,
	fundingPopupRoute,
	FundingTisProgrammesResolvePagingParams,
} from './';

const ENTITY_STATES = [
	...fundingRoute,
	...fundingPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		FundingTisProgrammesComponent,
		FundingTisProgrammesDetailComponent,
		FundingTisProgrammesDialogComponent,
		FundingTisProgrammesDeleteDialogComponent,
		FundingTisProgrammesPopupComponent,
		FundingTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		FundingTisProgrammesComponent,
		FundingTisProgrammesDialogComponent,
		FundingTisProgrammesPopupComponent,
		FundingTisProgrammesDeleteDialogComponent,
		FundingTisProgrammesDeletePopupComponent,
	],
	providers: [
		FundingTisProgrammesService,
		FundingTisProgrammesPopupService,
		FundingTisProgrammesResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsFundingTisProgrammesModule {
}
