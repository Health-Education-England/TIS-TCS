import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {PaginationUtil} from "ng-jhipster";
import {ProgrammeTisProgrammesComponent} from "./programme-tis-programmes.component";
import {ProgrammeTisProgrammesDetailComponent} from "./programme-tis-programmes-detail.component";
import {ProgrammeTisProgrammesPopupComponent} from "./programme-tis-programmes-dialog.component";
import {ProgrammeTisProgrammesDeletePopupComponent} from "./programme-tis-programmes-delete-dialog.component";

@Injectable()
export class ProgrammeTisProgrammesResolvePagingParams implements Resolve<any> {

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

export const programmeRoute: Routes = [
	{
		path: 'programme-tis-programmes',
		component: ProgrammeTisProgrammesComponent,
		resolve: {
			'pagingParams': ProgrammeTisProgrammesResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.programme.home.title'
		}
	}, {
		path: 'programme-tis-programmes/:id',
		component: ProgrammeTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.programme.home.title'
		}
	}
];

export const programmePopupRoute: Routes = [
	{
		path: 'programme-tis-programmes-new',
		component: ProgrammeTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.programme.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'programme-tis-programmes/:id/edit',
		component: ProgrammeTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.programme.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'programme-tis-programmes/:id/delete',
		component: ProgrammeTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.programme.home.title'
		},
		outlet: 'popup'
	}
];
