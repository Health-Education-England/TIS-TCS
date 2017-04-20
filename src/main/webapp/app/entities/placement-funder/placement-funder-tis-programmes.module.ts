import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TcsSharedModule} from '../../shared';
import {
	PlacementFunderTisProgrammesService,
	PlacementFunderTisProgrammesPopupService,
	PlacementFunderTisProgrammesComponent,
	PlacementFunderTisProgrammesDetailComponent,
	PlacementFunderTisProgrammesDialogComponent,
	PlacementFunderTisProgrammesPopupComponent,
	PlacementFunderTisProgrammesDeletePopupComponent,
	PlacementFunderTisProgrammesDeleteDialogComponent,
	placementFunderRoute,
	placementFunderPopupRoute,
	PlacementFunderTisProgrammesResolvePagingParams,
} from './';

const ENTITY_STATES = [
	...placementFunderRoute,
	...placementFunderPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		PlacementFunderTisProgrammesComponent,
		PlacementFunderTisProgrammesDetailComponent,
		PlacementFunderTisProgrammesDialogComponent,
		PlacementFunderTisProgrammesDeleteDialogComponent,
		PlacementFunderTisProgrammesPopupComponent,
		PlacementFunderTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		PlacementFunderTisProgrammesComponent,
		PlacementFunderTisProgrammesDialogComponent,
		PlacementFunderTisProgrammesPopupComponent,
		PlacementFunderTisProgrammesDeleteDialogComponent,
		PlacementFunderTisProgrammesDeletePopupComponent,
	],
	providers: [
		PlacementFunderTisProgrammesService,
		PlacementFunderTisProgrammesPopupService,
		PlacementFunderTisProgrammesResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsPlacementFunderTisProgrammesModule {
}
