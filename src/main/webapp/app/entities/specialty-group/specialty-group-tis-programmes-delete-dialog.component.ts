import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {SpecialtyGroupTisProgrammes} from "./specialty-group-tis-programmes.model";
import {SpecialtyGroupTisProgrammesPopupService} from "./specialty-group-tis-programmes-popup.service";
import {SpecialtyGroupTisProgrammesService} from "./specialty-group-tis-programmes.service";

@Component({
	selector: 'jhi-specialty-group-tis-programmes-delete-dialog',
	templateUrl: './specialty-group-tis-programmes-delete-dialog.component.html'
})
export class SpecialtyGroupTisProgrammesDeleteDialogComponent {

	specialtyGroup: SpecialtyGroupTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private specialtyGroupService: SpecialtyGroupTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['specialtyGroup']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.specialtyGroupService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'specialtyGroupListModification',
				content: 'Deleted an specialtyGroup'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-specialty-group-tis-programmes-delete-popup',
	template: ''
})
export class SpecialtyGroupTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private specialtyGroupPopupService: SpecialtyGroupTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.specialtyGroupPopupService
				.open(SpecialtyGroupTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
