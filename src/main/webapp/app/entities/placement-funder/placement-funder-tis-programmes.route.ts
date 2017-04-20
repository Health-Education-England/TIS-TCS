import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {PaginationUtil} from 'ng-jhipster';

import {PlacementFunderTisProgrammesComponent} from './placement-funder-tis-programmes.component';
import {PlacementFunderTisProgrammesDetailComponent} from './placement-funder-tis-programmes-detail.component';
import {PlacementFunderTisProgrammesPopupComponent} from './placement-funder-tis-programmes-dialog.component';
import {
	PlacementFunderTisProgrammesDeletePopupComponent
} from './placement-funder-tis-programmes-delete-dialog.component';

import {Principal} from '../../shared';

@Injectable()
export class PlacementFunderTisProgrammesResolvePagingParams implements Resolve<any> {

	constructor(private paginationUtil: PaginationUtil) {
	}

	resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
		const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
		return {
			page: this.paginationUtil.parsePage(page),
			predicate: this.paginationUtil.parsePredicate(sort),
			ascending: this.paginationUtil.parseAscending(sort)
		};
	}
}

export const placementFunderRoute: Routes = [
	{
		path: 'placement-funder-tis-programmes',
		component: PlacementFunderTisProgrammesComponent,
		resolve: {
			'pagingParams': PlacementFunderTisProgrammesResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.placementFunder.home.title'
		},
		canActivate: [UserRouteAccessService]
	}, {
		path: 'placement-funder-tis-programmes/:id',
		component: PlacementFunderTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.placementFunder.home.title'
		},
		canActivate: [UserRouteAccessService]
	}
];

export const placementFunderPopupRoute: Routes = [
	{
		path: 'placement-funder-tis-programmes-new',
		component: PlacementFunderTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.placementFunder.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'placement-funder-tis-programmes/:id/edit',
		component: PlacementFunderTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.placementFunder.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'placement-funder-tis-programmes/:id/delete',
		component: PlacementFunderTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.placementFunder.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	}
];
