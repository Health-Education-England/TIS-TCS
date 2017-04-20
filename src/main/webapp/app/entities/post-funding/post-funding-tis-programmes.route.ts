import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {PaginationUtil} from 'ng-jhipster';

import {PostFundingTisProgrammesComponent} from './post-funding-tis-programmes.component';
import {PostFundingTisProgrammesDetailComponent} from './post-funding-tis-programmes-detail.component';
import {PostFundingTisProgrammesPopupComponent} from './post-funding-tis-programmes-dialog.component';
import {PostFundingTisProgrammesDeletePopupComponent} from './post-funding-tis-programmes-delete-dialog.component';

import {Principal} from '../../shared';

@Injectable()
export class PostFundingTisProgrammesResolvePagingParams implements Resolve<any> {

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

export const postFundingRoute: Routes = [
	{
		path: 'post-funding-tis-programmes',
		component: PostFundingTisProgrammesComponent,
		resolve: {
			'pagingParams': PostFundingTisProgrammesResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.postFunding.home.title'
		},
		canActivate: [UserRouteAccessService]
	}, {
		path: 'post-funding-tis-programmes/:id',
		component: PostFundingTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.postFunding.home.title'
		},
		canActivate: [UserRouteAccessService]
	}
];

export const postFundingPopupRoute: Routes = [
	{
		path: 'post-funding-tis-programmes-new',
		component: PostFundingTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.postFunding.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'post-funding-tis-programmes/:id/edit',
		component: PostFundingTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.postFunding.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'post-funding-tis-programmes/:id/delete',
		component: PostFundingTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.postFunding.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	}
];
