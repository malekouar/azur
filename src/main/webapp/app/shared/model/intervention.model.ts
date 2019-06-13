import { Moment } from 'moment';
import { ILivraisons } from 'app/shared/model/livraisons.model';

export interface IIntervention {
  id?: number;
  type?: string;
  dateDebut?: Moment;
  dateFin?: Moment;
  responsable?: string;
  etat?: string;
  description?: string;
  detail?: string;
  dossierId?: number;
  livraisons?: ILivraisons[];
}

export class Intervention implements IIntervention {
  constructor(
    public id?: number,
    public type?: string,
    public dateDebut?: Moment,
    public dateFin?: Moment,
    public responsable?: string,
    public etat?: string,
    public description?: string,
    public detail?: string,
    public dossierId?: number,
    public livraisons?: ILivraisons[]
  ) {}
}
