package azur.support.web.tool.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link azur.support.web.tool.domain.Dossier} entity. This class is used
 * in {@link azur.support.web.tool.web.rest.DossierResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dossiers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DossierCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private InstantFilter dateDebut;

    private StringFilter responsable;

    private StringFilter etat;

    private StringFilter urlAzimut;

    private StringFilter urlRedmine;

    private StringFilter urlAkuiteo;

    private InstantFilter dateFin;

    private LongFilter clientId;

    private LongFilter interventionId;

    public DossierCriteria(){
    }

    public DossierCriteria(DossierCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.responsable = other.responsable == null ? null : other.responsable.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.urlAzimut = other.urlAzimut == null ? null : other.urlAzimut.copy();
        this.urlRedmine = other.urlRedmine == null ? null : other.urlRedmine.copy();
        this.urlAkuiteo = other.urlAkuiteo == null ? null : other.urlAkuiteo.copy();
        this.dateFin = other.dateFin == null ? null : other.dateFin.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
        this.interventionId = other.interventionId == null ? null : other.interventionId.copy();
    }

    @Override
    public DossierCriteria copy() {
        return new DossierCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getType() {
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public InstantFilter getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(InstantFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public StringFilter getResponsable() {
        return responsable;
    }

    public void setResponsable(StringFilter responsable) {
        this.responsable = responsable;
    }

    public StringFilter getEtat() {
        return etat;
    }

    public void setEtat(StringFilter etat) {
        this.etat = etat;
    }

    public StringFilter getUrlAzimut() {
        return urlAzimut;
    }

    public void setUrlAzimut(StringFilter urlAzimut) {
        this.urlAzimut = urlAzimut;
    }

    public StringFilter getUrlRedmine() {
        return urlRedmine;
    }

    public void setUrlRedmine(StringFilter urlRedmine) {
        this.urlRedmine = urlRedmine;
    }

    public StringFilter getUrlAkuiteo() {
        return urlAkuiteo;
    }

    public void setUrlAkuiteo(StringFilter urlAkuiteo) {
        this.urlAkuiteo = urlAkuiteo;
    }

    public InstantFilter getDateFin() {
        return dateFin;
    }

    public void setDateFin(InstantFilter dateFin) {
        this.dateFin = dateFin;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(LongFilter interventionId) {
        this.interventionId = interventionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DossierCriteria that = (DossierCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(responsable, that.responsable) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(urlAzimut, that.urlAzimut) &&
            Objects.equals(urlRedmine, that.urlRedmine) &&
            Objects.equals(urlAkuiteo, that.urlAkuiteo) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(interventionId, that.interventionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        dateDebut,
        responsable,
        etat,
        urlAzimut,
        urlRedmine,
        urlAkuiteo,
        dateFin,
        clientId,
        interventionId
        );
    }

    @Override
    public String toString() {
        return "DossierCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
                (responsable != null ? "responsable=" + responsable + ", " : "") +
                (etat != null ? "etat=" + etat + ", " : "") +
                (urlAzimut != null ? "urlAzimut=" + urlAzimut + ", " : "") +
                (urlRedmine != null ? "urlRedmine=" + urlRedmine + ", " : "") +
                (urlAkuiteo != null ? "urlAkuiteo=" + urlAkuiteo + ", " : "") +
                (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
                (interventionId != null ? "interventionId=" + interventionId + ", " : "") +
            "}";
    }

}
