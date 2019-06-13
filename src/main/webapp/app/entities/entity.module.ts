import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        loadChildren: './client/client.module#AzursupportwebtoolClientModule'
      },
      {
        path: 'config',
        loadChildren: './config/config.module#AzursupportwebtoolConfigModule'
      },
      {
        path: 'dossier',
        loadChildren: './dossier/dossier.module#AzursupportwebtoolDossierModule'
      },
      {
        path: 'intervention',
        loadChildren: './intervention/intervention.module#AzursupportwebtoolInterventionModule'
      },
      {
        path: 'livraisons',
        loadChildren: './livraisons/livraisons.module#AzursupportwebtoolLivraisonsModule'
      },
      {
        path: 'serveur',
        loadChildren: './serveur/serveur.module#AzursupportwebtoolServeurModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AzursupportwebtoolEntityModule {}
