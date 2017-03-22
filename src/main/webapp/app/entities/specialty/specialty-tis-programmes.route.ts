import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {PaginationUtil} from "ng-jhipster";
import {SpecialtyTisProgrammesComponent} from "./specialty-tis-programmes.component";
import {SpecialtyTisProgrammesDetailComponent} from "./specialty-tis-programmes-detail.component";
import {SpecialtyTisProgrammesPopupComponent} from "./specialty-tis-programmes-dialog.component";
import {SpecialtyTisProgrammesDeletePopupComponent} from "./specialty-tis-programmes-delete-dialog.component";

@Injectable()
export class SpecialtyTisProgrammesResolvePagingParams implements Resolve<any> {

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

export const specialtyRoute: Routes = [
	{
		path: 'specialty-tis-programmes',
		component: SpecialtyTisProgrammesComponent,
		resolve: {
			'pagingParams': SpecialtyTisProgrammesResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.specialty.home.title'
		}
	}, {
		path: 'specialty-tis-programmes/:id',
		component: SpecialtyTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.specialty.home.title'
		}
	}
];

export const specialtyPopupRoute: Routes = [
	{
		path: 'specialty-tis-programmes-new',
		component: SpecialtyTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.specialty.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'specialty-tis-programmes/:id/edit',
		component: SpecialtyTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.specialty.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'specialty-tis-programmes/:id/delete',
		component: SpecialtyTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.specialty.home.title'
		},
		outlet: 'popup'
	}
];
