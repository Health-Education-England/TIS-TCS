import {Injectable} from '@angular/core';
import {Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate} from '@angular/router';

import {UserRouteAccessService} from '../../shared';
import {PaginationUtil} from 'ng-jhipster';

import {TariffRateTisProgrammesComponent} from './tariff-rate-tis-programmes.component';
import {TariffRateTisProgrammesDetailComponent} from './tariff-rate-tis-programmes-detail.component';
import {TariffRateTisProgrammesPopupComponent} from './tariff-rate-tis-programmes-dialog.component';
import {TariffRateTisProgrammesDeletePopupComponent} from './tariff-rate-tis-programmes-delete-dialog.component';

import {Principal} from '../../shared';

export const tariffRateRoute: Routes = [
	{
		path: 'tariff-rate-tis-programmes',
		component: TariffRateTisProgrammesComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.tariffRate.home.title'
		},
		canActivate: [UserRouteAccessService]
	}, {
		path: 'tariff-rate-tis-programmes/:id',
		component: TariffRateTisProgrammesDetailComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.tariffRate.home.title'
		},
		canActivate: [UserRouteAccessService]
	}
];

export const tariffRatePopupRoute: Routes = [
	{
		path: 'tariff-rate-tis-programmes-new',
		component: TariffRateTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.tariffRate.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'tariff-rate-tis-programmes/:id/edit',
		component: TariffRateTisProgrammesPopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.tariffRate.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	},
	{
		path: 'tariff-rate-tis-programmes/:id/delete',
		component: TariffRateTisProgrammesDeletePopupComponent,
		data: {
			authorities: ['ROLE_USER'],
			pageTitle: 'tcsApp.tariffRate.home.title'
		},
		canActivate: [UserRouteAccessService],
		outlet: 'popup'
	}
];
