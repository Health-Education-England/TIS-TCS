import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {PaginationUtil} from 'ng-jhipster';

import {PlacementTisProgrammesComponent} from './placement-tis-programmes.component';
import {PlacementTisProgrammesDetailComponent} from './placement-tis-programmes-detail.component';
import {PlacementTisProgrammesPopupComponent} from './placement-tis-programmes-dialog.component';
import {PlacementTisProgrammesDeletePopupComponent} from './placement-tis-programmes-delete-dialog.component';

import {Principal} from '../../shared';

@Injectable()
export class PlacementTisProgrammesResolvePagingParams implements Resolve<any> {

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

export const placementRoute: Routes = [
	{
		path: 'placement-tis-programmes',
		component: PlacementTisProgrammesComponent,
		resolve: {
			'pagingParams': PlacementTisProgrammesResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.placement.home.title'
		},
		canActivate: [UserRouteAccessService]
	}, {
		path: 'placement-tis-programmes/:id',
		component: PlacementTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.placement.home.title'
		},
		canActivate: [UserRouteAccessService]
	}
];

export const placementPopupRoute: Routes = [
	{
		path: 'placement-tis-programmes-new',
		component: PlacementTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.placement.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'placement-tis-programmes/:id/edit',
		component: PlacementTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.placement.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'placement-tis-programmes/:id/delete',
		component: PlacementTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.placement.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	}
];
