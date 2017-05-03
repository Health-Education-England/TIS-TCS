import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TcsSharedModule} from '../../shared';
import {
	PostFundingTisProgrammesService,
	PostFundingTisProgrammesPopupService,
	PostFundingTisProgrammesComponent,
	PostFundingTisProgrammesDetailComponent,
	PostFundingTisProgrammesDialogComponent,
	PostFundingTisProgrammesPopupComponent,
	PostFundingTisProgrammesDeletePopupComponent,
	PostFundingTisProgrammesDeleteDialogComponent,
	postFundingRoute,
	postFundingPopupRoute,
	PostFundingTisProgrammesResolvePagingParams,
} from './';

const ENTITY_STATES = [
	...postFundingRoute,
	...postFundingPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		PostFundingTisProgrammesComponent,
		PostFundingTisProgrammesDetailComponent,
		PostFundingTisProgrammesDialogComponent,
		PostFundingTisProgrammesDeleteDialogComponent,
		PostFundingTisProgrammesPopupComponent,
		PostFundingTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		PostFundingTisProgrammesComponent,
		PostFundingTisProgrammesDialogComponent,
		PostFundingTisProgrammesPopupComponent,
		PostFundingTisProgrammesDeleteDialogComponent,
		PostFundingTisProgrammesDeletePopupComponent,
	],
	providers: [
		PostFundingTisProgrammesService,
		PostFundingTisProgrammesPopupService,
		PostFundingTisProgrammesResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsPostFundingTisProgrammesModule {
}
