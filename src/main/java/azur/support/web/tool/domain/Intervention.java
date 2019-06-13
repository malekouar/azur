package azur.support.web.tool.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Intervention.
 */
@Entity
@Table(name = "intervention")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "intervention")
public class Intervention implements Serializable {

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
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @Column(name = "date_fin")
    private Instant dateFin;

    @NotNull
    @Size(max = 45)
    @Column(name = "responsable", length = 45, nullable = false)
    private String responsable;

    @NotNull
    @Size(max = 45)
    @Column(name = "etat", length = 45, nullable = false)
    private String etat;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Size(max = 255)
    @Column(name = "detail", length = 255)
    private String detail;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("interventions")
    private Dossier dossier;

    @OneToMany(mappedBy = "intervention")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Livraisons> livraisons = new HashSet<>();

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

    public Intervention type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public Intervention dateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public Intervention dateFin(Instant dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public String getResponsable() {
        return responsable;
    }

    public Intervention responsable(String responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getEtat() {
        return etat;
    }

    public Intervention etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getDescription() {
        return description;
    }

    public Intervention description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public Intervention detail(String detail) {
        this.detail = detail;
        return this;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Dossier getDossier() {
        return dossier;
    }

    public Intervention dossier(Dossier dossier) {
        this.dossier = dossier;
        return this;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public Set<Livraisons> getLivraisons() {
        return livraisons;
    }

    public Intervention livraisons(Set<Livraisons> livraisons) {
        this.livraisons = livraisons;
        return this;
    }

    public Intervention addLivraisons(Livraisons livraisons) {
        this.livraisons.add(livraisons);
        livraisons.setIntervention(this);
        return this;
    }

    public Intervention removeLivraisons(Livraisons livraisons) {
        this.livraisons.remove(livraisons);
        livraisons.setIntervention(null);
        return this;
    }

    public void setLivraisons(Set<Livraisons> livraisons) {
        this.livraisons = livraisons;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Intervention)) {
            return false;
        }
        return id != null && id.equals(((Intervention) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Intervention{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", etat='" + getEtat() + "'" +
            ", description='" + getDescription() + "'" +
            ", detail='" + getDetail() + "'" +
            "}";
    }
}
