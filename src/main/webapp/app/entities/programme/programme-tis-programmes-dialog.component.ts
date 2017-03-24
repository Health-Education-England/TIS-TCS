import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {ProgrammeTisProgrammes} from "./programme-tis-programmes.model";
import {ProgrammeTisProgrammesPopupService} from "./programme-tis-programmes-popup.service";
import {ProgrammeTisProgrammesService} from "./programme-tis-programmes.service";
import {ProgrammeMembershipTisProgrammes, ProgrammeMembershipTisProgrammesService} from "../programme-membership";
@Component({
	selector: 'jhi-programme-tis-programmes-dialog',
	templateUrl: './programme-tis-programmes-dialog.component.html'
})
export class ProgrammeTisProgrammesDialogComponent implements OnInit {

	programme: ProgrammeTisProgrammes;
	authorities: any[];
	isSaving: boolean;

	programmememberships: ProgrammeMembershipTisProgrammes[];

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private programmeService: ProgrammeTisProgrammesService,
				private programmeMembershipService: ProgrammeMembershipTisProgrammesService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['programme', 'status']);
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
		if (this.programme.id !== undefined) {
			this.programmeService.update(this.programme)
				.subscribe((res: ProgrammeTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.programmeService.create(this.programme)
				.subscribe((res: ProgrammeTisProgrammes) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: ProgrammeTisProgrammes) {
		this.eventManager.broadcast({name: 'programmeListModification', content: 'OK'});
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
	selector: 'jhi-programme-tis-programmes-popup',
	template: ''
})
export class ProgrammeTisProgrammesPopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private programmePopupService: ProgrammeTisProgrammesPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.programmePopupService
					.open(ProgrammeTisProgrammesDialogComponent, params['id']);
			} else {
				this.modalRef = this.programmePopupService
					.open(ProgrammeTisProgrammesDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
