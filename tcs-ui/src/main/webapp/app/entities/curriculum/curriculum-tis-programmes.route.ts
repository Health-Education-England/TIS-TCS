import {Injectable} from "@angular/core";
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes} from "@angular/router";
import {PaginationUtil} from "ng-jhipster";
import {CurriculumTisProgrammesComponent} from "./curriculum-tis-programmes.component";
import {CurriculumTisProgrammesDetailComponent} from "./curriculum-tis-programmes-detail.component";
import {CurriculumTisProgrammesPopupComponent} from "./curriculum-tis-programmes-dialog.component";
import {CurriculumTisProgrammesDeletePopupComponent} from "./curriculum-tis-programmes-delete-dialog.component";

@Injectable()
export class CurriculumTisProgrammesResolvePagingParams implements Resolve<any> {

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

export const curriculumRoute: Routes = [
	{
		path: 'curriculum-tis-programmes',
		component: CurriculumTisProgrammesComponent,
		resolve: {
			'pagingParams': CurriculumTisProgrammesResolvePagingParams
		},
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.curriculum.home.title'
		}
	}, {
		path: 'curriculum-tis-programmes/:id',
		component: CurriculumTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.curriculum.home.title'
		}
	}
];

export const curriculumPopupRoute: Routes = [
	{
		path: 'curriculum-tis-programmes-new',
		component: CurriculumTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.curriculum.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'curriculum-tis-programmes/:id/edit',
		component: CurriculumTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.curriculum.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'curriculum-tis-programmes/:id/delete',
		component: CurriculumTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.curriculum.home.title'
		},
		outlet: 'popup'
	}
];
