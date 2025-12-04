import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPeregrinos, NewPeregrinos } from '../peregrinos.model';

export type PartialUpdatePeregrinos = Partial<IPeregrinos> & Pick<IPeregrinos, 'id'>;

export type EntityResponseType = HttpResponse<IPeregrinos>;
export type EntityArrayResponseType = HttpResponse<IPeregrinos[]>;

@Injectable({ providedIn: 'root' })
export class PeregrinosService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/peregrinos');

  create(peregrinos: NewPeregrinos): Observable<EntityResponseType> {
    return this.http.post<IPeregrinos>(this.resourceUrl, peregrinos, { observe: 'response' });
  }

  update(peregrinos: IPeregrinos): Observable<EntityResponseType> {
    return this.http.put<IPeregrinos>(`${this.resourceUrl}/${this.getPeregrinosIdentifier(peregrinos)}`, peregrinos, {
      observe: 'response',
    });
  }

  partialUpdate(peregrinos: PartialUpdatePeregrinos): Observable<EntityResponseType> {
    return this.http.patch<IPeregrinos>(`${this.resourceUrl}/${this.getPeregrinosIdentifier(peregrinos)}`, peregrinos, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPeregrinos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPeregrinos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPeregrinosIdentifier(peregrinos: Pick<IPeregrinos, 'id'>): number {
    return peregrinos.id;
  }

  comparePeregrinos(o1: Pick<IPeregrinos, 'id'> | null, o2: Pick<IPeregrinos, 'id'> | null): boolean {
    return o1 && o2 ? this.getPeregrinosIdentifier(o1) === this.getPeregrinosIdentifier(o2) : o1 === o2;
  }

  addPeregrinosToCollectionIfMissing<Type extends Pick<IPeregrinos, 'id'>>(
    peregrinosCollection: Type[],
    ...peregrinosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const peregrinos: Type[] = peregrinosToCheck.filter(isPresent);
    if (peregrinos.length > 0) {
      const peregrinosCollectionIdentifiers = peregrinosCollection.map(peregrinosItem => this.getPeregrinosIdentifier(peregrinosItem));
      const peregrinosToAdd = peregrinos.filter(peregrinosItem => {
        const peregrinosIdentifier = this.getPeregrinosIdentifier(peregrinosItem);
        if (peregrinosCollectionIdentifiers.includes(peregrinosIdentifier)) {
          return false;
        }
        peregrinosCollectionIdentifiers.push(peregrinosIdentifier);
        return true;
      });
      return [...peregrinosToAdd, ...peregrinosCollection];
    }
    return peregrinosCollection;
  }
}
