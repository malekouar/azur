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
 * Criteria class for the {@link azur.support.web.tool.domain.Livraisons} entity. This class is used
 * in {@link azur.support.web.tool.web.rest.LivraisonsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /livraisons?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LivraisonsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter type;

    private InstantFilter dateLivraison;

    private StringFilter responsable;

    private StringFilter etat;

    private StringFilter nomPackage;

    private IntegerFilter idSvn;

    private StringFilter description;

    private StringFilter detail;

    private LongFilter interventionId;

    public LivraisonsCriteria(){
    }

    public LivraisonsCriteria(LivraisonsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.dateLivraison = other.dateLivraison == null ? null : other.dateLivraison.copy();
        this.responsable = other.responsable == null ? null : other.responsable.copy();
        this.etat = other.etat == null ? null : other.etat.copy();
        this.nomPackage = other.nomPackage == null ? null : other.nomPackage.copy();
        this.idSvn = other.idSvn == null ? null : other.idSvn.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.detail = other.detail == null ? null : other.detail.copy();
        this.interventionId = other.interventionId == null ? null : other.interventionId.copy();
    }

    @Override
    public LivraisonsCriteria copy() {
        return new LivraisonsCriteria(this);
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

    public InstantFilter getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(InstantFilter dateLivraison) {
        this.dateLivraison = dateLivraison;
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

    public StringFilter getNomPackage() {
        return nomPackage;
    }

    public void setNomPackage(StringFilter nomPackage) {
        this.nomPackage = nomPackage;
    }

    public IntegerFilter getIdSvn() {
        return idSvn;
    }

    public void setIdSvn(IntegerFilter idSvn) {
        this.idSvn = idSvn;
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
        final LivraisonsCriteria that = (LivraisonsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(dateLivraison, that.dateLivraison) &&
            Objects.equals(responsable, that.responsable) &&
            Objects.equals(etat, that.etat) &&
            Objects.equals(nomPackage, that.nomPackage) &&
            Objects.equals(idSvn, that.idSvn) &&
            Objects.equals(description, that.description) &&
            Objects.equals(detail, that.detail) &&
            Objects.equals(interventionId, that.interventionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        dateLivraison,
        responsable,
        etat,
        nomPackage,
        idSvn,
        description,
        detail,
        interventionId
        );
    }

    @Override
    public String toString() {
        return "LivraisonsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (dateLivraison != null ? "dateLivraison=" + dateLivraison + ", " : "") +
                (responsable != null ? "responsable=" + responsable + ", " : "") +
                (etat != null ? "etat=" + etat + ", " : "") +
                (nomPackage != null ? "nomPackage=" + nomPackage + ", " : "") +
                (idSvn != null ? "idSvn=" + idSvn + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (detail != null ? "detail=" + detail + ", " : "") +
                (interventionId != null ? "interventionId=" + interventionId + ", " : "") +
            "}";
    }

}
