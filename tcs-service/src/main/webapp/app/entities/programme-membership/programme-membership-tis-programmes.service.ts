import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {ProgrammeMembershipTisProgrammes} from "./programme-membership-tis-programmes.model";
import {DateUtils} from "ng-jhipster";
@Injectable()
export class ProgrammeMembershipTisProgrammesService {

	private resourceUrl = 'api/programme-memberships';

	constructor(private http: Http, private dateUtils: DateUtils) {
	}

	create(programmeMembership: ProgrammeMembershipTisProgrammes): Observable<ProgrammeMembershipTisProgrammes> {

		let copy: ProgrammeMembershipTisProgrammes = Object.assign({}, programmeMembership);
		copy.curriculumStartDate = this.dateUtils
			.convertLocalDateToServer(programmeMembership.curriculumStartDate);
		copy.curriculumEndDate = this.dateUtils
			.convertLocalDateToServer(programmeMembership.curriculumEndDate);
		copy.programmeStartDate = this.dateUtils
			.convertLocalDateToServer(programmeMembership.programmeStartDate);
		copy.curriculumCompletionDate = this.dateUtils
			.convertLocalDateToServer(programmeMembership.curriculumCompletionDate);
		copy.programmeEndDate = this.dateUtils
			.convertLocalDateToServer(programmeMembership.programmeEndDate);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(programmeMembership: ProgrammeMembershipTisProgrammes): Observable<ProgrammeMembershipTisProgrammes> {

		let copy: ProgrammeMembershipTisProgrammes = Object.assign({}, programmeMembership);
		copy.curriculumStartDate = this.dateUtils
			.convertLocalDateToServer(programmeMembership.curriculumStartDate);
		copy.curriculumEndDate = this.dateUtils
			.convertLocalDateToServer(programmeMembership.curriculumEndDate);
		copy.programmeStartDate = this.dateUtils
			.convertLocalDateToServer(programmeMembership.programmeStartDate);
		copy.curriculumCompletionDate = this.dateUtils
			.convertLocalDateToServer(programmeMembership.curriculumCompletionDate);
		copy.programmeEndDate = this.dateUtils
			.convertLocalDateToServer(programmeMembership.programmeEndDate);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<ProgrammeMembershipTisProgrammes> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			let jsonResponse = res.json();
			jsonResponse.curriculumStartDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.curriculumStartDate);
			jsonResponse.curriculumEndDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.curriculumEndDate);
			jsonResponse.programmeStartDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.programmeStartDate);
			jsonResponse.curriculumCompletionDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.curriculumCompletionDate);
			jsonResponse.programmeEndDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.programmeEndDate);
			return jsonResponse;
		});
	}

	query(req?: any): Observable<Response> {
		let options = this.createRequestOption(req);
		return this.http.get(this.resourceUrl, options)
			.map((res: any) => this.convertResponse(res))
			;
	}

	delete(id: number): Observable<Response> {
		return this.http.delete(`${this.resourceUrl}/${id}`);
	}


	private convertResponse(res: any): any {
		let jsonResponse = res.json();
		for (let i = 0; i < jsonResponse.length; i++) {
			jsonResponse[i].curriculumStartDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].curriculumStartDate);
			jsonResponse[i].curriculumEndDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].curriculumEndDate);
			jsonResponse[i].programmeStartDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].programmeStartDate);
			jsonResponse[i].curriculumCompletionDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].curriculumCompletionDate);
			jsonResponse[i].programmeEndDate = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].programmeEndDate);
		}
		res._body = jsonResponse;
		return res;
	}

	private createRequestOption(req?: any): BaseRequestOptions {
		let options: BaseRequestOptions = new BaseRequestOptions();
		if (req) {
			let params: URLSearchParams = new URLSearchParams();
			params.set('page', req.page);
			params.set('size', req.size);
			if (req.sort) {
				params.paramsMap.set('sort', req.sort);
			}
			params.set('query', req.query);

			options.search = params;
		}
		return options;
	}
}
