export interface IServeur {
  id?: number;
  serveurType?: string;
  serveurNom?: string;
  serveurIp?: string;
  login?: string;
  password?: string;
  configId?: number;
}

export class Serveur implements IServeur {
  constructor(
    public id?: number,
    public serveurType?: string,
    public serveurNom?: string,
    public serveurIp?: string,
    public login?: string,
    public password?: string,
    public configId?: number
  ) {}
}
