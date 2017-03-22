import {Routes} from "@angular/router";
import {GradeTisProgrammesComponent} from "./grade-tis-programmes.component";
import {GradeTisProgrammesDetailComponent} from "./grade-tis-programmes-detail.component";
import {GradeTisProgrammesPopupComponent} from "./grade-tis-programmes-dialog.component";
import {GradeTisProgrammesDeletePopupComponent} from "./grade-tis-programmes-delete-dialog.component";


export const gradeRoute: Routes = [
	{
		path: 'grade-tis-programmes',
		component: GradeTisProgrammesComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.grade.home.title'
		}
	}, {
		path: 'grade-tis-programmes/:id',
		component: GradeTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.grade.home.title'
		}
	}
];

export const gradePopupRoute: Routes = [
	{
		path: 'grade-tis-programmes-new',
		component: GradeTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.grade.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'grade-tis-programmes/:id/edit',
		component: GradeTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.grade.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'grade-tis-programmes/:id/delete',
		component: GradeTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.grade.home.title'
		},
		outlet: 'popup'
	}
];
