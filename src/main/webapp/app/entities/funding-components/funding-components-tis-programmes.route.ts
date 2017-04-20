import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {PaginationUtil} from 'ng-jhipster';

import {FundingComponentsTisProgrammesComponent} from './funding-components-tis-programmes.component';
import {FundingComponentsTisProgrammesDetailComponent} from './funding-components-tis-programmes-detail.component';
import {FundingComponentsTisProgrammesPopupComponent} from './funding-components-tis-programmes-dialog.component';
import {
	FundingComponentsTisProgrammesDeletePopupComponent
} from './funding-components-tis-programmes-delete-dialog.component';

import {Principal} from '../../shared';

@Injectable()
export class FundingComponentsTisProgrammesResolvePagingParams implements Resolve<any> {

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

export const fundingComponentsRoute: Routes = [
	{
		path: 'funding-components-tis-programmes',
		component: FundingComponentsTisProgrammesComponent,
		resolve: {
			'pagingParams': FundingComponentsTisProgrammesResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.fundingComponents.home.title'
		},
		canActivate: [UserRouteAccessService]
	}, {
		path: 'funding-components-tis-programmes/:id',
		component: FundingComponentsTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.fundingComponents.home.title'
		},
		canActivate: [UserRouteAccessService]
	}
];

export const fundingComponentsPopupRoute: Routes = [
	{
		path: 'funding-components-tis-programmes-new',
		component: FundingComponentsTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.fundingComponents.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'funding-components-tis-programmes/:id/edit',
		component: FundingComponentsTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.fundingComponents.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'funding-components-tis-programmes/:id/delete',
		component: FundingComponentsTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.fundingComponents.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	}
];
