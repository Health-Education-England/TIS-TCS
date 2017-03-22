import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {PaginationUtil} from "ng-jhipster";
import {ProgrammeMembershipTisProgrammesComponent} from "./programme-membership-tis-programmes.component";
import {ProgrammeMembershipTisProgrammesDetailComponent} from "./programme-membership-tis-programmes-detail.component";
import {ProgrammeMembershipTisProgrammesPopupComponent} from "./programme-membership-tis-programmes-dialog.component";
import {ProgrammeMembershipTisProgrammesDeletePopupComponent} from "./programme-membership-tis-programmes-delete-dialog.component";

@Injectable()
export class ProgrammeMembershipTisProgrammesResolvePagingParams implements Resolve<any> {

	constructor(private paginationUtil: PaginationUtil) {
	}

	resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		let page = route.queryParams['page'] ? route.queryParams['page'] : '1';
		let sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
		return {
			page: this.paginationUtil.parsePage(page),
			predicate: this.paginationUtil.parsePredicate(sort),
			ascending: this.paginationUtil.parseAscending(sort)
		};
	}
}

export const programmeMembershipRoute: Routes = [
	{
		path: 'programme-membership-tis-programmes',
		component: ProgrammeMembershipTisProgrammesComponent,
		resolve: {
			'pagingParams': ProgrammeMembershipTisProgrammesResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.programmeMembership.home.title'
		}
	}, {
		path: 'programme-membership-tis-programmes/:id',
		component: ProgrammeMembershipTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.programmeMembership.home.title'
		}
	}
];

export const programmeMembershipPopupRoute: Routes = [
	{
		path: 'programme-membership-tis-programmes-new',
		component: ProgrammeMembershipTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.programmeMembership.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'programme-membership-tis-programmes/:id/edit',
		component: ProgrammeMembershipTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.programmeMembership.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'programme-membership-tis-programmes/:id/delete',
		component: ProgrammeMembershipTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.programmeMembership.home.title'
		},
		outlet: 'popup'
	}
];
