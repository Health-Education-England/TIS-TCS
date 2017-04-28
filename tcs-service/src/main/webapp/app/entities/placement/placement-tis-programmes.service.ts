import {Injectable} from '@angular/core';
import {Http, Response, URLSearchParams, BaseRequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {PlacementTisProgrammes} from './placement-tis-programmes.model';
import {DateUtils} from 'ng-jhipster';
@Injectable()
export class PlacementTisProgrammesService {

	private resourceUrl = 'api/placements';

	constructor(private http: Http, private dateUtils: DateUtils) {
	}

	create(placement: PlacementTisProgrammes): Observable<PlacementTisProgrammes> {
		const copy: PlacementTisProgrammes = Object.assign({}, placement);
		copy.dateFrom = this.dateUtils
		.convertLocalDateToServer(placement.dateFrom);
		copy.dateTo = this.dateUtils
		.convertLocalDateToServer(placement.dateTo);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(placement: PlacementTisProgrammes): Observable<PlacementTisProgrammes> {
		const copy: PlacementTisProgrammes = Object.assign({}, placement);
		copy.dateFrom = this.dateUtils
		.convertLocalDateToServer(placement.dateFrom);
		copy.dateTo = this.dateUtils
		.convertLocalDateToServer(placement.dateTo);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<PlacementTisProgrammes> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			const jsonResponse = res.json();
			jsonResponse.dateFrom = this.dateUtils
			.convertLocalDateFromServer(jsonResponse.dateFrom);
			jsonResponse.dateTo = this.dateUtils
			.convertLocalDateFromServer(jsonResponse.dateTo);
			return jsonResponse;
		});
	}

	query(req?: any): Observable<Response> {
		const options = this.createRequestOption(req);
		return this.http.get(this.resourceUrl, options)
		.map((res: any) => this.convertResponse(res))
				;
	}

	delete(id: number): Observable<Response> {
		return this.http.delete(`${this.resourceUrl}/${id}`);
	}

	private convertResponse(res: any): any {
		const jsonResponse = res.json();
		for (let i = 0; i < jsonResponse.length; i++) {
			jsonResponse[i].dateFrom = this.dateUtils
			.convertLocalDateFromServer(jsonResponse[i].dateFrom);
			jsonResponse[i].dateTo = this.dateUtils
			.convertLocalDateFromServer(jsonResponse[i].dateTo);
		}
		res._body = jsonResponse;
		return res;
	}

	private createRequestOption(req?: any): BaseRequestOptions {
		const options: BaseRequestOptions = new BaseRequestOptions();
		if (req) {
			const params: URLSearchParams = new URLSearchParams();
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
