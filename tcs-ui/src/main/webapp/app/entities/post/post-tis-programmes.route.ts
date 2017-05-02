import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {PaginationUtil} from 'ng-jhipster';

import {PostTisProgrammesComponent} from './post-tis-programmes.component';
import {PostTisProgrammesDetailComponent} from './post-tis-programmes-detail.component';
import {PostTisProgrammesPopupComponent} from './post-tis-programmes-dialog.component';
import {PostTisProgrammesDeletePopupComponent} from './post-tis-programmes-delete-dialog.component';

import {Principal} from '../../shared';

@Injectable()
export class PostTisProgrammesResolvePagingParams implements Resolve<any> {

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

export const postRoute: Routes = [
	{
		path: 'post-tis-programmes',
		component: PostTisProgrammesComponent,
		resolve: {
			'pagingParams': PostTisProgrammesResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.post.home.title'
		},
		canActivate: [UserRouteAccessService]
	}, {
		path: 'post-tis-programmes/:id',
		component: PostTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.post.home.title'
		},
		canActivate: [UserRouteAccessService]
	}
];

export const postPopupRoute: Routes = [
	{
		path: 'post-tis-programmes-new',
		component: PostTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.post.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'post-tis-programmes/:id/edit',
		component: PostTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.post.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'post-tis-programmes/:id/delete',
		component: PostTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.post.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	}
];
