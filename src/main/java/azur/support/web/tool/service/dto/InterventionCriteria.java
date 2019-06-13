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
 * Criteria class for the {@link azur.support.web.tool.domain.Intervention} entity. This class is used
 * in {@link azur.support.web.tool.web.rest.InterventionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /interventions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class InterventionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private InstantFilter dateDebut;

    private InstantFilter dateFin;

    private StringFilter responsable;

    private StringFilter etat;

    private StringFilter description;

    private StringFilter detail;

    private LongFilter dossierId;

    private LongFilter livraisonsId;

    public InterventionCriteria(){
    }

    public InterventionCriteria(InterventionCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.dateFin = other.dateFin == null ? null : other.dateFin.copy();
        this.responsable = other.responsable == null ? null : other.responsable.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.detail = other.detail == null ? null : other.detail.copy();
        this.dossierId = other.dossierId == null ? null : other.dossierId.copy();
        this.livraisonsId = other.livraisonsId == null ? null : other.livraisonsId.copy();
    }

    @Override
    public InterventionCriteria copy() {
        return new InterventionCriteria(this);
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

    public InstantFilter getDateFin() {
        return dateFin;
    }

    public void setDateFin(InstantFilter dateFin) {
        this.dateFin = dateFin;
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

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getDetail() {
        return detail;
    }

    public void setDetail(StringFilter detail) {
        this.detail = detail;
    }

    public LongFilter getDossierId() {
        return dossierId;
    }

    public void setDossierId(LongFilter dossierId) {
        this.dossierId = dossierId;
    }

    public LongFilter getLivraisonsId() {
        return livraisonsId;
    }

    public void setLivraisonsId(LongFilter livraisonsId) {
        this.livraisonsId = livraisonsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final InterventionCriteria that = (InterventionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(responsable, that.responsable) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(description, that.description) &&
            Objects.equals(detail, that.detail) &&
            Objects.equals(dossierId, that.dossierId) &&
            Objects.equals(livraisonsId, that.livraisonsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        dateDebut,
        dateFin,
        responsable,
        etat,
        description,
        detail,
        dossierId,
        livraisonsId
        );
    }

    @Override
    public String toString() {
        return "InterventionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
                (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
                (responsable != null ? "responsable=" + responsable + ", " : "") +
                (etat != null ? "etat=" + etat + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (detail != null ? "detail=" + detail + ", " : "") +
                (dossierId != null ? "dossierId=" + dossierId + ", " : "") +
                (livraisonsId != null ? "livraisonsId=" + livraisonsId + ", " : "") +
            "}";
    }

}
