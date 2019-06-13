import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILivraisons } from 'app/shared/model/livraisons.model';

@Component({
  selector: 'jhi-livraisons-detail',
  templateUrl: './livraisons-detail.component.html'
})
export class LivraisonsDetailComponent implements OnInit {
  livraisons: ILivraisons;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ livraisons }) => {
      this.livraisons = livraisons;
    });
  }

  previousState() {
    window.history.back();
  }
}
