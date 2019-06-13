/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { LivraisonsService } from 'app/entities/livraisons/livraisons.service';
import { ILivraisons, Livraisons } from 'app/shared/model/livraisons.model';

describe('Service Tests', () => {
  describe('Livraisons Service', () => {
    let injector: TestBed;
    let service: LivraisonsService;
    let httpMock: HttpTestingController;
    let elemDefault: ILivraisons;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(LivraisonsService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Livraisons(0, 'AAAAAAA', currentDate, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            dateLivraison: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a Livraisons', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateLivraison: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateLivraison: currentDate
          },
          returnedFromService
        );
        service
          .create(new Livraisons(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Livraisons', async () => {
        const returnedFromService = Object.assign(
          {
            type: 'BBBBBB',
            dateLivraison: currentDate.format(DATE_TIME_FORMAT),
            responsable: 'BBBBBB',
            etat: 'BBBBBB',
            nomPackage: 'BBBBBB',
            idSvn: 1,
            description: 'BBBBBB',
            detail: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateLivraison: currentDate
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

      it('should return a list of Livraisons', async () => {
        const returnedFromService = Object.assign(
          {
            type: 'BBBBBB',
            dateLivraison: currentDate.format(DATE_TIME_FORMAT),
            responsable: 'BBBBBB',
            etat: 'BBBBBB',
            nomPackage: 'BBBBBB',
            idSvn: 1,
            description: 'BBBBBB',
            detail: 'BBBBBB'
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            dateLivraison: currentDate
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

      it('should delete a Livraisons', async () => {
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
