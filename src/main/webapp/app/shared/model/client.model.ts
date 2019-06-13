import { IConfig } from 'app/shared/model/config.model';
import { IDossier } from 'app/shared/model/dossier.model';

export interface IClient {
  id?: number;
  raisonSociale?: string;
  contact?: string;
  tel?: string;
  mobile?: string;
  email?: string;
  configs?: IConfig[];
  dossiers?: IDossier[];
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public raisonSociale?: string,
    public contact?: string,
    public tel?: string,
    public mobile?: string,
    public email?: string,
    public configs?: IConfig[],
    public dossiers?: IDossier[]
  ) {}
}
