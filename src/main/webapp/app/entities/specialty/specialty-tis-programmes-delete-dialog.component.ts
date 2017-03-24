import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {SpecialtyTisProgrammes} from "./specialty-tis-programmes.model";
import {SpecialtyTisProgrammesPopupService} from "./specialty-tis-programmes-popup.service";
import {SpecialtyTisProgrammesService} from "./specialty-tis-programmes.service";

@Component({
	selector: 'jhi-specialty-tis-programmes-delete-dialog',
	templateUrl: './specialty-tis-programmes-delete-dialog.component.html'
})
export class SpecialtyTisProgrammesDeleteDialogComponent {

	specialty: SpecialtyTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private specialtyService: SpecialtyTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['specialty', 'status', 'specialtyType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.specialtyService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'specialtyListModification',
				content: 'Deleted an specialty'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-specialty-tis-programmes-delete-popup',
	template: ''
})
export class SpecialtyTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private specialtyPopupService: SpecialtyTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.specialtyPopupService
				.open(SpecialtyTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
