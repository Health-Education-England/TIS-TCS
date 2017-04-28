import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {PaginationUtil} from 'ng-jhipster';

import {FundingTisProgrammesComponent} from './funding-tis-programmes.component';
import {FundingTisProgrammesDetailComponent} from './funding-tis-programmes-detail.component';
import {FundingTisProgrammesPopupComponent} from './funding-tis-programmes-dialog.component';
import {FundingTisProgrammesDeletePopupComponent} from './funding-tis-programmes-delete-dialog.component';

import {Principal} from '../../shared';

@Injectable()
export class FundingTisProgrammesResolvePagingParams implements Resolve<any> {

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

export const fundingRoute: Routes = [
	{
		path: 'funding-tis-programmes',
		component: FundingTisProgrammesComponent,
		resolve: {
			'pagingParams': FundingTisProgrammesResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.funding.home.title'
		},
		canActivate: [UserRouteAccessService]
	}, {
		path: 'funding-tis-programmes/:id',
		component: FundingTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.funding.home.title'
		},
		canActivate: [UserRouteAccessService]
	}
];

export const fundingPopupRoute: Routes = [
	{
		path: 'funding-tis-programmes-new',
		component: FundingTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.funding.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'funding-tis-programmes/:id/edit',
		component: FundingTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.funding.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'funding-tis-programmes/:id/delete',
		component: FundingTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.funding.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	}
];
