import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { SpecialtyGroupTisProgrammesComponent } from './specialty-group-tis-programmes.component';
import { SpecialtyGroupTisProgrammesDetailComponent } from './specialty-group-tis-programmes-detail.component';
import { SpecialtyGroupTisProgrammesPopupComponent } from './specialty-group-tis-programmes-dialog.component';
import { SpecialtyGroupTisProgrammesDeletePopupComponent } from './specialty-group-tis-programmes-delete-dialog.component';

import { Principal } from '../../shared';


export const specialtyGroupRoute: Routes = [
  {
    path: 'specialty-group-tis-programmes',
    component: SpecialtyGroupTisProgrammesComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'tcsApp.specialtyGroup.home.title'
    }
  }, {
    path: 'specialty-group-tis-programmes/:id',
    component: SpecialtyGroupTisProgrammesDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'tcsApp.specialtyGroup.home.title'
    }
  }
];

export const specialtyGroupPopupRoute: Routes = [
  {
    path: 'specialty-group-tis-programmes-new',
    component: SpecialtyGroupTisProgrammesPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'tcsApp.specialtyGroup.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'specialty-group-tis-programmes/:id/edit',
    component: SpecialtyGroupTisProgrammesPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'tcsApp.specialtyGroup.home.title'
    },
    outlet: 'popup'
  },
  {
    path: 'specialty-group-tis-programmes/:id/delete',
    component: SpecialtyGroupTisProgrammesDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'tcsApp.specialtyGroup.home.title'
    },
    outlet: 'popup'
  }
];
