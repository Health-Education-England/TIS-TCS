import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {CurriculumTisProgrammes} from "./curriculum-tis-programmes.model";
import {DateUtils} from "ng-jhipster";
@Injectable()
export class CurriculumTisProgrammesService {

	private resourceUrl = 'api/curricula';

	constructor(private http: Http, private dateUtils: DateUtils) {
	}

	create(curriculum: CurriculumTisProgrammes): Observable<CurriculumTisProgrammes> {
		let copy: CurriculumTisProgrammes = Object.assign({}, curriculum);
		copy.start = this.dateUtils
			.convertLocalDateToServer(curriculum.start);
		copy.end = this.dateUtils
			.convertLocalDateToServer(curriculum.end);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(curriculum: CurriculumTisProgrammes): Observable<CurriculumTisProgrammes> {
		let copy: CurriculumTisProgrammes = Object.assign({}, curriculum);
		copy.start = this.dateUtils
			.convertLocalDateToServer(curriculum.start);
		copy.end = this.dateUtils
			.convertLocalDateToServer(curriculum.end);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<CurriculumTisProgrammes> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			let jsonResponse = res.json();
			jsonResponse.start = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.start);
			jsonResponse.end = this.dateUtils
				.convertLocalDateFromServer(jsonResponse.end);
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
			jsonResponse[i].start = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].start);
			jsonResponse[i].end = this.dateUtils
				.convertLocalDateFromServer(jsonResponse[i].end);
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
