package azur.support.web.tool.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Livraisons.
 */
@Entity
@Table(name = "livraisons")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "livraisons")
public class Livraisons implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(max = 45)
    @Column(name = "type", length = 45, nullable = false)
    private String type;

    @NotNull
    @Column(name = "date_livraison", nullable = false)
    private Instant dateLivraison;

    @NotNull
    @Size(max = 45)
    @Column(name = "responsable", length = 45, nullable = false)
    private String responsable;

    @NotNull
    @Size(max = 45)
    @Column(name = "etat", length = 45, nullable = false)
    private String etat;

    @NotNull
    @Size(max = 255)
    @Column(name = "nom_package", length = 255, nullable = false)
    private String nomPackage;

    @NotNull
    @Column(name = "id_svn", nullable = false)
    private Integer idSvn;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Size(max = 255)
    @Column(name = "detail", length = 255)
    private String detail;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("livraisons")
    private Intervention intervention;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public Livraisons type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getDateLivraison() {
        return dateLivraison;
    }

    public Livraisons dateLivraison(Instant dateLivraison) {
        this.dateLivraison = dateLivraison;
        return this;
    }

    public void setDateLivraison(Instant dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public String getResponsable() {
        return responsable;
    }

    public Livraisons responsable(String responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getEtat() {
        return etat;
    }

    public Livraisons etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getNomPackage() {
        return nomPackage;
    }

    public Livraisons nomPackage(String nomPackage) {
        this.nomPackage = nomPackage;
        return this;
    }

    public void setNomPackage(String nomPackage) {
        this.nomPackage = nomPackage;
    }

    public Integer getIdSvn() {
        return idSvn;
    }

    public Livraisons idSvn(Integer idSvn) {
        this.idSvn = idSvn;
        return this;
    }

    public void setIdSvn(Integer idSvn) {
        this.idSvn = idSvn;
    }

    public String getDescription() {
        return description;
    }

    public Livraisons description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public Livraisons detail(String detail) {
        this.detail = detail;
        return this;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Intervention getIntervention() {
        return intervention;
    }

    public Livraisons intervention(Intervention intervention) {
        this.intervention = intervention;
        return this;
    }

    public void setIntervention(Intervention intervention) {
        this.intervention = intervention;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Livraisons)) {
            return false;
        }
        return id != null && id.equals(((Livraisons) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Livraisons{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", dateLivraison='" + getDateLivraison() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", etat='" + getEtat() + "'" +
            ", nomPackage='" + getNomPackage() + "'" +
            ", idSvn=" + getIdSvn() +
            ", description='" + getDescription() + "'" +
            ", detail='" + getDetail() + "'" +
            "}";
    }
}
