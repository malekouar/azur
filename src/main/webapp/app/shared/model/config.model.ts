import { IServeur } from 'app/shared/model/serveur.model';

export interface IConfig {
  id?: number;
  teamviewerId?: string;
  teamviewerPassword?: string;
  vpnType?: string;
  vpnIp?: string;
  vpnLogin?: string;
  vpnPassword?: string;
  clientId?: number;
  serveurs?: IServeur[];
}

export class Config implements IConfig {
  constructor(
    public id?: number,
    public teamviewerId?: string,
    public teamviewerPassword?: string,
    public vpnType?: string,
    public vpnIp?: string,
    public vpnLogin?: string,
    public vpnPassword?: string,
    public clientId?: number,
    public serveurs?: IServeur[]
  ) {}
}
