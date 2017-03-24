import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {TrainingNumberTisProgrammes} from "./training-number-tis-programmes.model";
import {TrainingNumberTisProgrammesPopupService} from "./training-number-tis-programmes-popup.service";
import {TrainingNumberTisProgrammesService} from "./training-number-tis-programmes.service";

@Component({
	selector: 'jhi-training-number-tis-programmes-delete-dialog',
	templateUrl: './training-number-tis-programmes-delete-dialog.component.html'
})
export class TrainingNumberTisProgrammesDeleteDialogComponent {

	trainingNumber: TrainingNumberTisProgrammes;

	constructor(private jhiLanguageService: JhiLanguageService,
				private trainingNumberService: TrainingNumberTisProgrammesService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['trainingNumber', 'trainingNumberType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.trainingNumberService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'trainingNumberListModification',
				content: 'Deleted an trainingNumber'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-training-number-tis-programmes-delete-popup',
	template: ''
})
export class TrainingNumberTisProgrammesDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private trainingNumberPopupService: TrainingNumberTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.trainingNumberPopupService
				.open(TrainingNumberTisProgrammesDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
