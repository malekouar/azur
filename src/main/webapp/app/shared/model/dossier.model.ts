import { Moment } from 'moment';
import { IIntervention } from 'app/shared/model/intervention.model';

export interface IDossier {
  id?: number;
  type?: string;
  dateDebut?: Moment;
  responsable?: string;
  etat?: string;
  urlAzimut?: string;
  urlRedmine?: string;
  urlAkuiteo?: string;
  dateFin?: Moment;
  clientId?: number;
  interventions?: IIntervention[];
}

export class Dossier implements IDossier {
  constructor(
    public id?: number,
    public type?: string,
    public dateDebut?: Moment,
    public responsable?: string,
    public etat?: string,
    public urlAzimut?: string,
    public urlRedmine?: string,
    public urlAkuiteo?: string,
    public dateFin?: Moment,
    public clientId?: number,
    public interventions?: IIntervention[]
  ) {}
}
