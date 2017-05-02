import {Injectable} from '@angular/core';
import {Http, Response, URLSearchParams, BaseRequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Rx';

import {TariffFundingTypeFieldsTisProgrammes} from './tariff-funding-type-fields-tis-programmes.model';
import {DateUtils} from 'ng-jhipster';
@Injectable()
export class TariffFundingTypeFieldsTisProgrammesService {

	private resourceUrl = 'api/tariff-funding-type-fields';

	constructor(private http: Http, private dateUtils: DateUtils) {
	}

	create(tariffFundingTypeFields: TariffFundingTypeFieldsTisProgrammes): Observable<TariffFundingTypeFieldsTisProgrammes> {
		const copy: TariffFundingTypeFieldsTisProgrammes = Object.assign({}, tariffFundingTypeFields);
		copy.effectiveDateFrom = this.dateUtils
		.convertLocalDateToServer(tariffFundingTypeFields.effectiveDateFrom);
		copy.effectiveDateTo = this.dateUtils
		.convertLocalDateToServer(tariffFundingTypeFields.effectiveDateTo);
		return this.http.post(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	update(tariffFundingTypeFields: TariffFundingTypeFieldsTisProgrammes): Observable<TariffFundingTypeFieldsTisProgrammes> {
		const copy: TariffFundingTypeFieldsTisProgrammes = Object.assign({}, tariffFundingTypeFields);
		copy.effectiveDateFrom = this.dateUtils
		.convertLocalDateToServer(tariffFundingTypeFields.effectiveDateFrom);
		copy.effectiveDateTo = this.dateUtils
		.convertLocalDateToServer(tariffFundingTypeFields.effectiveDateTo);
		return this.http.put(this.resourceUrl, copy).map((res: Response) => {
			return res.json();
		});
	}

	find(id: number): Observable<TariffFundingTypeFieldsTisProgrammes> {
		return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
			const jsonResponse = res.json();
			jsonResponse.effectiveDateFrom = this.dateUtils
			.convertLocalDateFromServer(jsonResponse.effectiveDateFrom);
			jsonResponse.effectiveDateTo = this.dateUtils
			.convertLocalDateFromServer(jsonResponse.effectiveDateTo);
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
			jsonResponse[i].effectiveDateFrom = this.dateUtils
			.convertLocalDateFromServer(jsonResponse[i].effectiveDateFrom);
			jsonResponse[i].effectiveDateTo = this.dateUtils
			.convertLocalDateFromServer(jsonResponse[i].effectiveDateTo);
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
