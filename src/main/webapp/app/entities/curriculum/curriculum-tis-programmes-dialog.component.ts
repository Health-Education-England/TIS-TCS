import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {CurriculumTisProgrammes} from "./curriculum-tis-programmes.model";
import {CurriculumTisProgrammesPopupService} from "./curriculum-tis-programmes-popup.service";
import {CurriculumTisProgrammesService} from "./curriculum-tis-programmes.service";
import {ProgrammeMembershipTisProgrammes, ProgrammeMembershipTisProgrammesService} from "../programme-membership";
import {GradeTisProgrammes, GradeTisProgrammesService} from "../grade";
import {SpecialtyTisProgrammes, SpecialtyTisProgrammesService} from "../specialty";
@Component({
	selector: 'jhi-curriculum-tis-programmes-dialog',
	templateUrl: './curriculum-tis-programmes-dialog.component.html'
})
export class CurriculumTisProgrammesDialogComponent implements OnInit {

	curriculum: CurriculumTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	programmememberships: ProgrammeMembershipTisProgrammes[];

	grades: GradeTisProgrammes[];

	specialties: SpecialtyTisProgrammes[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private curriculumService: CurriculumTisProgrammesService,
				private programmeMembershipService: ProgrammeMembershipTisProgrammesService,
				private gradeService: GradeTisProgrammesService,
				private specialtyService: SpecialtyTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['curriculum', 'curriculumSubType', 'assessmentType']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
		this.programmeMembershipService.query().subscribe(
			(res: Response) => {
				this.programmememberships = res.json();
			}, (res: Response) => this.onError(res.json()));
		this.gradeService.query().subscribe(
			(res: Response) => {
				this.grades = res.json();
			}, (res: Response) => this.onError(res.json()));
		this.specialtyService.query().subscribe(
			(res: Response) => {
				this.specialties = res.json();
			}, (res: Response) => this.onError(res.json()));
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.curriculum.id !== undefined) {
			this.curriculumService.update(this.curriculum)
				.subscribe((res: CurriculumTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.curriculumService.create(this.curriculum)
				.subscribe((res: CurriculumTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: CurriculumTisProgrammes) {
		this.eventManager.broadcast({name: 'curriculumListModification', content: 'OK'});
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

	trackGradeById(index: number, item: GradeTisProgrammes) {
		return item.id;
	}

	trackSpecialtyById(index: number, item: SpecialtyTisProgrammes) {
		return item.id;
	}

	getSelected(selectedVals: Array<any>, option: any) {
		if (selectedVals) {
			for (let i = 0; i < selectedVals.length; i++) {
				if (option.id === selectedVals[i].id) {
					return selectedVals[i];
				}
			}
		}
		return option;
	}
}

@Component({
	selector: 'jhi-curriculum-tis-programmes-popup',
	template: ''
})
export class CurriculumTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private curriculumPopupService: CurriculumTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.curriculumPopupService
					.open(CurriculumTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.curriculumPopupService
					.open(CurriculumTisProgrammesDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
