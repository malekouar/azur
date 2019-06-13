import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIntervention } from 'app/shared/model/intervention.model';

@Component({
  selector: 'jhi-intervention-detail',
  templateUrl: './intervention-detail.component.html'
})
export class InterventionDetailComponent implements OnInit {
  intervention: IIntervention;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ intervention }) => {
      this.intervention = intervention;
    });
  }

  previousState() {
    window.history.back();
  }
}
