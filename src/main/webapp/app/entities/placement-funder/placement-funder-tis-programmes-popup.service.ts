import {Injectable, Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {PlacementFunderTisProgrammes} from './placement-funder-tis-programmes.model';
import {PlacementFunderTisProgrammesService} from './placement-funder-tis-programmes.service';
@Injectable()
export class PlacementFunderTisProgrammesPopupService {
	private isOpen = false;

	constructor(private modalService: NgbModal,
				private router: Router,
				private placementFunderService: PlacementFunderTisProgrammesService) {
	}

	open(component: Component, id?: number | any): NgbModalRef {
		if (this.isOpen) {
			return;
		}
		this.isOpen = true;

		if (id) {
			this.placementFunderService.find(id).subscribe((placementFunder) => {
				this.placementFunderModalRef(component, placementFunder);
			});
		} else {
			return this.placementFunderModalRef(component, new PlacementFunderTisProgrammes());
		}
	}

	placementFunderModalRef(component: Component, placementFunder: PlacementFunderTisProgrammes): NgbModalRef {
		const modalRef = this.modalService.open(component, {size: 'lg', backdrop: 'static'});
		modalRef.componentInstance.placementFunder = placementFunder;
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
