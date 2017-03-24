import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {SpecialtyTisProgrammes} from "./specialty-tis-programmes.model";
import {SpecialtyTisProgrammesPopupService} from "./specialty-tis-programmes-popup.service";
import {SpecialtyTisProgrammesService} from "./specialty-tis-programmes.service";
import {CurriculumTisProgrammes, CurriculumTisProgrammesService} from "../curriculum";
import {SpecialtyGroupTisProgrammes, SpecialtyGroupTisProgrammesService} from "../specialty-group";
@Component({
	selector: 'jhi-specialty-tis-programmes-dialog',
	templateUrl: './specialty-tis-programmes-dialog.component.html'
})
export class SpecialtyTisProgrammesDialogComponent implements OnInit {

	specialty: SpecialtyTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	curricula: CurriculumTisProgrammes[];

	specialtygroups: SpecialtyGroupTisProgrammes[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private specialtyService: SpecialtyTisProgrammesService,
				private curriculumService: CurriculumTisProgrammesService,
				private specialtyGroupService: SpecialtyGroupTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['specialty', 'status', 'specialtyType']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
		this.curriculumService.query().subscribe(
			(res: Response) => {
				this.curricula = res.json();
			}, (res: Response) => this.onError(res.json()));
		this.specialtyGroupService.query().subscribe(
			(res: Response) => {
				this.specialtygroups = res.json();
			}, (res: Response) => this.onError(res.json()));
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.specialty.id !== undefined) {
			this.specialtyService.update(this.specialty)
				.subscribe((res: SpecialtyTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.specialtyService.create(this.specialty)
				.subscribe((res: SpecialtyTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: SpecialtyTisProgrammes) {
		this.eventManager.broadcast({name: 'specialtyListModification', content: 'OK'});
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

	trackCurriculumById(index: number, item: CurriculumTisProgrammes) {
		return item.id;
	}

	trackSpecialtyGroupById(index: number, item: SpecialtyGroupTisProgrammes) {
		return item.id;
	}
}

@Component({
	selector: 'jhi-specialty-tis-programmes-popup',
	template: ''
})
export class SpecialtyTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private specialtyPopupService: SpecialtyTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.specialtyPopupService
					.open(SpecialtyTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.specialtyPopupService
					.open(SpecialtyTisProgrammesDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
