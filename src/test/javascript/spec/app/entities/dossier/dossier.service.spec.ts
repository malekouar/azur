/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { DossierService } from 'app/entities/dossier/dossier.service';
import { IDossier, Dossier } from 'app/shared/model/dossier.model';

describe('Service Tests', () => {
  describe('Dossier Service', () => {
    let injector: TestBed;
    let service: DossierService;
    let httpMock: HttpTestingController;
    let elemDefault: IDossier;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(DossierService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Dossier(0, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            dateFin: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Dossier', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            dateFin: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate
          },
          returnedFromService
        );
        service
          .create(new Dossier(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Dossier', async () => {
        const returnedFromService = Object.assign(
          {
            type: 'BBBBBB',
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            responsable: 'BBBBBB',
            etat: 'BBBBBB',
            urlAzimut: 'BBBBBB',
            urlRedmine: 'BBBBBB',
            urlAkuiteo: 'BBBBBB',
            dateFin: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Dossier', async () => {
        const returnedFromService = Object.assign(
          {
            type: 'BBBBBB',
            dateDebut: currentDate.format(DATE_TIME_FORMAT),
            responsable: 'BBBBBB',
            etat: 'BBBBBB',
            urlAzimut: 'BBBBBB',
            urlRedmine: 'BBBBBB',
            urlAkuiteo: 'BBBBBB',
            dateFin: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateDebut: currentDate,
            dateFin: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Dossier', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
