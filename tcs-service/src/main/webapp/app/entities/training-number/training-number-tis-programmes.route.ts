import {Routes} from "@angular/router";
import {TrainingNumberTisProgrammesComponent} from "./training-number-tis-programmes.component";
import {TrainingNumberTisProgrammesDetailComponent} from "./training-number-tis-programmes-detail.component";
import {TrainingNumberTisProgrammesPopupComponent} from "./training-number-tis-programmes-dialog.component";
import {TrainingNumberTisProgrammesDeletePopupComponent} from "./training-number-tis-programmes-delete-dialog.component";


export const trainingNumberRoute: Routes = [
	{
		path: 'training-number-tis-programmes',
		component: TrainingNumberTisProgrammesComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.trainingNumber.home.title'
		}
	}, {
		path: 'training-number-tis-programmes/:id',
		component: TrainingNumberTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.trainingNumber.home.title'
		}
	}
];

export const trainingNumberPopupRoute: Routes = [
	{
		path: 'training-number-tis-programmes-new',
		component: TrainingNumberTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.trainingNumber.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'training-number-tis-programmes/:id/edit',
		component: TrainingNumberTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.trainingNumber.home.title'
		},
		outlet: 'popup'
	},
	{
		path: 'training-number-tis-programmes/:id/delete',
		component: TrainingNumberTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.trainingNumber.home.title'
		},
		outlet: 'popup'
	}
];
