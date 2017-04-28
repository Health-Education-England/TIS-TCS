import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {PaginationUtil} from 'ng-jhipster';

import {TariffFundingTypeFieldsTisProgrammesComponent} from './tariff-funding-type-fields-tis-programmes.component';
import {TariffFundingTypeFieldsTisProgrammesDetailComponent} from './tariff-funding-type-fields-tis-programmes-detail.component';
import {TariffFundingTypeFieldsTisProgrammesPopupComponent} from './tariff-funding-type-fields-tis-programmes-dialog.component';
import {
	TariffFundingTypeFieldsTisProgrammesDeletePopupComponent
} from './tariff-funding-type-fields-tis-programmes-delete-dialog.component';

import {Principal} from '../../shared';

export const tariffFundingTypeFieldsRoute: Routes = [
	{
		path: 'tariff-funding-type-fields-tis-programmes',
		component: TariffFundingTypeFieldsTisProgrammesComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.tariffFundingTypeFields.home.title'
		},
		canActivate: [UserRouteAccessService]
	}, {
		path: 'tariff-funding-type-fields-tis-programmes/:id',
		component: TariffFundingTypeFieldsTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.tariffFundingTypeFields.home.title'
		},
		canActivate: [UserRouteAccessService]
	}
];

export const tariffFundingTypeFieldsPopupRoute: Routes = [
	{
		path: 'tariff-funding-type-fields-tis-programmes-new',
		component: TariffFundingTypeFieldsTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.tariffFundingTypeFields.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'tariff-funding-type-fields-tis-programmes/:id/edit',
		component: TariffFundingTypeFieldsTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.tariffFundingTypeFields.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'tariff-funding-type-fields-tis-programmes/:id/delete',
		component: TariffFundingTypeFieldsTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.tariffFundingTypeFields.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	}
];
