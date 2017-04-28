import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {RouterModule} from '@angular/router';

import {TcsSharedModule} from '../../shared';
import {
	PlacementTisProgrammesService,
	PlacementTisProgrammesPopupService,
	PlacementTisProgrammesComponent,
	PlacementTisProgrammesDetailComponent,
	PlacementTisProgrammesDialogComponent,
	PlacementTisProgrammesPopupComponent,
	PlacementTisProgrammesDeletePopupComponent,
	PlacementTisProgrammesDeleteDialogComponent,
	placementRoute,
	placementPopupRoute,
	PlacementTisProgrammesResolvePagingParams,
} from './';

const ENTITY_STATES = [
	...placementRoute,
	...placementPopupRoute,
];

@NgModule({
	imports: [
		TcsSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		PlacementTisProgrammesComponent,
		PlacementTisProgrammesDetailComponent,
		PlacementTisProgrammesDialogComponent,
		PlacementTisProgrammesDeleteDialogComponent,
		PlacementTisProgrammesPopupComponent,
		PlacementTisProgrammesDeletePopupComponent,
	],
	entryComponents: [
		PlacementTisProgrammesComponent,
		PlacementTisProgrammesDialogComponent,
		PlacementTisProgrammesPopupComponent,
		PlacementTisProgrammesDeleteDialogComponent,
		PlacementTisProgrammesDeletePopupComponent,
	],
	providers: [
		PlacementTisProgrammesService,
		PlacementTisProgrammesPopupService,
		PlacementTisProgrammesResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TcsPlacementTisProgrammesModule {
}
