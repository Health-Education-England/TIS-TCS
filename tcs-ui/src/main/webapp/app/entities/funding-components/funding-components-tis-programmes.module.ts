import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TcsSharedModule} from '../../shared';
import {
	FundingComponentsTisProgrammesService,
	FundingComponentsTisProgrammesPopupService,
	FundingComponentsTisProgrammesComponent,
	FundingComponentsTisProgrammesDetailComponent,
	FundingComponentsTisProgrammesDialogComponent,
	FundingComponentsTisProgrammesPopupComponent,
	FundingComponentsTisProgrammesDeletePopupComponent,
	FundingComponentsTisProgrammesDeleteDialogComponent,
	fundingComponentsRoute,
	fundingComponentsPopupRoute,
	FundingComponentsTisProgrammesResolvePagingParams,
} from './';

const ENTITY_STATES = [
	...fundingComponentsRoute,
	...fundingComponentsPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		FundingComponentsTisProgrammesComponent,
		FundingComponentsTisProgrammesDetailComponent,
		FundingComponentsTisProgrammesDialogComponent,
		FundingComponentsTisProgrammesDeleteDialogComponent,
		FundingComponentsTisProgrammesPopupComponent,
		FundingComponentsTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		FundingComponentsTisProgrammesComponent,
		FundingComponentsTisProgrammesDialogComponent,
		FundingComponentsTisProgrammesPopupComponent,
		FundingComponentsTisProgrammesDeleteDialogComponent,
		FundingComponentsTisProgrammesDeletePopupComponent,
	],
	providers: [
		FundingComponentsTisProgrammesService,
		FundingComponentsTisProgrammesPopupService,
		FundingComponentsTisProgrammesResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsFundingComponentsTisProgrammesModule {
}
