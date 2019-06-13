import { Moment } from 'moment';

export interface ILivraisons {
  id?: number;
  type?: string;
  dateLivraison?: Moment;
  responsable?: string;
  etat?: string;
  nomPackage?: string;
  idSvn?: number;
  description?: string;
  detail?: string;
  interventionId?: number;
}

export class Livraisons implements ILivraisons {
  constructor(
    public id?: number,
    public type?: string,
    public dateLivraison?: Moment,
    public responsable?: string,
    public etat?: string,
    public nomPackage?: string,
    public idSvn?: number,
    public description?: string,
    public detail?: string,
    public interventionId?: number
  ) {}
}
