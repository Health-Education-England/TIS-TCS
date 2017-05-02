import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {PlacementTisProgrammes} from './placement-tis-programmes.model';
import {PlacementTisProgrammesService} from './placement-tis-programmes.service';
@Injectable()
export class PlacementTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private placementService: PlacementTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.placementService.find(id).subscribe((placement) => {
				if (placement.dateFrom) {
					placement.dateFrom = {
						year: placement.dateFrom.getFullYear(),
						month: placement.dateFrom.getMonth() + 1,
						day: placement.dateFrom.getDate()
					};
				}
				if (placement.dateTo) {
					placement.dateTo = {
						year: placement.dateTo.getFullYear(),
						month: placement.dateTo.getMonth() + 1,
						day: placement.dateTo.getDate()
					};
				}
				this.placementModalRef(component, placement);
			});
		} else {
			return this.placementModalRef(component, new PlacementTisProgrammes());
		}
	}

	placementModalRef(component: Component, placement: PlacementTisProgrammes): NgbModalRef {
		const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.placement = placement;
		modalRef.result.then((result) => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		}, (reason) => {
			this.router.navigate([{outlets: {popup: null}}], {replaceUrl: true});
			this.isOpen = false;
		});
		return modalRef;
	}
}
