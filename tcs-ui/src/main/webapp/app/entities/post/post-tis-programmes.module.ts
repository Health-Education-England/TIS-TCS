import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TcsSharedModule} from '../../shared';
import {
	PostTisProgrammesService,
	PostTisProgrammesPopupService,
	PostTisProgrammesComponent,
	PostTisProgrammesDetailComponent,
	PostTisProgrammesDialogComponent,
	PostTisProgrammesPopupComponent,
	PostTisProgrammesDeletePopupComponent,
	PostTisProgrammesDeleteDialogComponent,
	postRoute,
	postPopupRoute,
	PostTisProgrammesResolvePagingParams,
} from './';

const ENTITY_STATES = [
	...postRoute,
	...postPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		PostTisProgrammesComponent,
		PostTisProgrammesDetailComponent,
		PostTisProgrammesDialogComponent,
		PostTisProgrammesDeleteDialogComponent,
		PostTisProgrammesPopupComponent,
		PostTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		PostTisProgrammesComponent,
		PostTisProgrammesDialogComponent,
		PostTisProgrammesPopupComponent,
		PostTisProgrammesDeleteDialogComponent,
		PostTisProgrammesDeletePopupComponent,
	],
	providers: [
		PostTisProgrammesService,
		PostTisProgrammesPopupService,
		PostTisProgrammesResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsPostTisProgrammesModule {
}
