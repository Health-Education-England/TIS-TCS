import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {ProgrammeMembershipTisProgrammes} from "./programme-membership-tis-programmes.model";
import {ProgrammeMembershipTisProgrammesPopupService} from "./programme-membership-tis-programmes-popup.service";
import {ProgrammeMembershipTisProgrammesService} from "./programme-membership-tis-programmes.service";
import {ProgrammeTisProgrammes, ProgrammeTisProgrammesService} from "../programme";
import {CurriculumTisProgrammes, CurriculumTisProgrammesService} from "../curriculum";
import {TrainingNumberTisProgrammes, TrainingNumberTisProgrammesService} from "../training-number";
@Component({
	selector: 'jhi-programme-membership-tis-programmes-dialog',
	templateUrl: './programme-membership-tis-programmes-dialog.component.html'
})
export class ProgrammeMembershipTisProgrammesDialogComponent implements OnInit {

	programmeMembership: ProgrammeMembershipTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	programmes: ProgrammeTisProgrammes[];

	curricula: CurriculumTisProgrammes[];

	trainingnumbers: TrainingNumberTisProgrammes[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private programmeMembershipService: ProgrammeMembershipTisProgrammesService,
				private programmeService: ProgrammeTisProgrammesService,
				private curriculumService: CurriculumTisProgrammesService,
				private trainingNumberService: TrainingNumberTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['programmeMembership', 'programmeMembershipType']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
		this.programmeService.query().subscribe(
			(res: Response) => {
				this.programmes = res.json();
			}, (res: Response) => this.onError(res.json()));
		this.curriculumService.query().subscribe(
			(res: Response) => {
				this.curricula = res.json();
			}, (res: Response) => this.onError(res.json()));
		this.trainingNumberService.query().subscribe(
			(res: Response) => {
				this.trainingnumbers = res.json();
			}, (res: Response) => this.onError(res.json()));
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.programmeMembership.id !== undefined) {
			this.programmeMembershipService.update(this.programmeMembership)
				.subscribe((res: ProgrammeMembershipTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.programmeMembershipService.create(this.programmeMembership)
				.subscribe((res: ProgrammeMembershipTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: ProgrammeMembershipTisProgrammes) {
		this.eventManager.broadcast({name: 'programmeMembershipListModification', content: 'OK'});
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

	trackProgrammeById(index: number, item: ProgrammeTisProgrammes) {
		return item.id;
	}

	trackCurriculumById(index: number, item: CurriculumTisProgrammes) {
		return item.id;
	}

	trackTrainingNumberById(index: number, item: TrainingNumberTisProgrammes) {
		return item.id;
	}
}

@Component({
	selector: 'jhi-programme-membership-tis-programmes-popup',
	template: ''
})
export class ProgrammeMembershipTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private programmeMembershipPopupService: ProgrammeMembershipTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.programmeMembershipPopupService
					.open(ProgrammeMembershipTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.programmeMembershipPopupService
					.open(ProgrammeMembershipTisProgrammesDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
