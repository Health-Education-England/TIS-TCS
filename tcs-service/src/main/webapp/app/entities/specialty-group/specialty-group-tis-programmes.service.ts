import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, BaseRequestOptions} from "@angular/http";
import {Observable} from "rxjs/Rx";
import {SpecialtyGroupTisProgrammes} from "./specialty-group-tis-programmes.model";
@Injectable()
export class SpecialtyGroupTisProgrammesService {

	private resourceUrl = 'api/specialty-groups';

	constructor(private http: Http) {
	}

	create(specialtyGroup: SpecialtyGroupTisProgrammes): Observable<SpecialtyGroupTisProgrammes> {
		let copy: SpecialtyGroupTisProgrammes = Object.assign({}, specialtyGroup);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(specialtyGroup: SpecialtyGroupTisProgrammes): Observable<SpecialtyGroupTisProgrammes> {
		let copy: SpecialtyGroupTisProgrammes = Object.assign({}, specialtyGroup);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<SpecialtyGroupTisProgrammes> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			return res.json();
		});
	}

	query(req?: any): Observable<Response> {
		let options = this.createRequestOption(req);
		return this.http.get(this.resourceUrl, options)
			;
	}

	delete(id: number): Observable<Response> {
		return this.http.delete(`${this.resourceUrl}/${id}`);
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
