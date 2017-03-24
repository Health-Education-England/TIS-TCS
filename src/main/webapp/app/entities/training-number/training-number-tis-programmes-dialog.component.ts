import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {TrainingNumberTisProgrammes} from "./training-number-tis-programmes.model";
import {TrainingNumberTisProgrammesPopupService} from "./training-number-tis-programmes-popup.service";
import {TrainingNumberTisProgrammesService} from "./training-number-tis-programmes.service";
import {ProgrammeMembershipTisProgrammes, ProgrammeMembershipTisProgrammesService} from "../programme-membership";
@Component({
	selector: 'jhi-training-number-tis-programmes-dialog',
	templateUrl: './training-number-tis-programmes-dialog.component.html'
})
export class TrainingNumberTisProgrammesDialogComponent implements OnInit {

	trainingNumber: TrainingNumberTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	programmememberships: ProgrammeMembershipTisProgrammes[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private trainingNumberService: TrainingNumberTisProgrammesService,
				private programmeMembershipService: ProgrammeMembershipTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['trainingNumber', 'trainingNumberType']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
		this.programmeMembershipService.query().subscribe(
			(res: Response) => {
				this.programmememberships = res.json();
			}, (res: Response) => this.onError(res.json()));
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.trainingNumber.id !== undefined) {
			this.trainingNumberService.update(this.trainingNumber)
				.subscribe((res: TrainingNumberTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.trainingNumberService.create(this.trainingNumber)
				.subscribe((res: TrainingNumberTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: TrainingNumberTisProgrammes) {
		this.eventManager.broadcast({name: 'trainingNumberListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}

	trackProgrammeMembershipById(index: number, item: ProgrammeMembershipTisProgrammes) {
		return item.id;
	}
}

@Component({
	selector: 'jhi-training-number-tis-programmes-popup',
	template: ''
})
export class TrainingNumberTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private trainingNumberPopupService: TrainingNumberTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.trainingNumberPopupService
					.open(TrainingNumberTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.trainingNumberPopupService
					.open(TrainingNumberTisProgrammesDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
