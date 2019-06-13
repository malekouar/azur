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
 * A Dossier.
 */
@Entity
@Table(name = "dossier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dossier")
public class Dossier implements Serializable {

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

    @NotNull
    @Size(max = 45)
    @Column(name = "responsable", length = 45, nullable = false)
    private String responsable;

    @NotNull
    @Size(max = 45)
    @Column(name = "etat", length = 45, nullable = false)
    private String etat;

    @Size(max = 255)
    @Column(name = "url_azimut", length = 255)
    private String urlAzimut;

    @Size(max = 255)
    @Column(name = "url_redmine", length = 255)
    private String urlRedmine;

    @Size(max = 255)
    @Column(name = "url_akuiteo", length = 255)
    private String urlAkuiteo;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private Instant dateFin;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("dossiers")
    private Client client;

    @OneToMany(mappedBy = "dossier")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Intervention> interventions = new HashSet<>();

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

    public Dossier type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public Dossier dateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getResponsable() {
        return responsable;
    }

    public Dossier responsable(String responsable) {
        this.responsable = responsable;
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getEtat() {
        return etat;
    }

    public Dossier etat(String etat) {
        this.etat = etat;
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getUrlAzimut() {
        return urlAzimut;
    }

    public Dossier urlAzimut(String urlAzimut) {
        this.urlAzimut = urlAzimut;
        return this;
    }

    public void setUrlAzimut(String urlAzimut) {
        this.urlAzimut = urlAzimut;
    }

    public String getUrlRedmine() {
        return urlRedmine;
    }

    public Dossier urlRedmine(String urlRedmine) {
        this.urlRedmine = urlRedmine;
        return this;
    }

    public void setUrlRedmine(String urlRedmine) {
        this.urlRedmine = urlRedmine;
    }

    public String getUrlAkuiteo() {
        return urlAkuiteo;
    }

    public Dossier urlAkuiteo(String urlAkuiteo) {
        this.urlAkuiteo = urlAkuiteo;
        return this;
    }

    public void setUrlAkuiteo(String urlAkuiteo) {
        this.urlAkuiteo = urlAkuiteo;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public Dossier dateFin(Instant dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public Client getClient() {
        return client;
    }

    public Dossier client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Intervention> getInterventions() {
        return interventions;
    }

    public Dossier interventions(Set<Intervention> interventions) {
        this.interventions = interventions;
        return this;
    }

    public Dossier addIntervention(Intervention intervention) {
        this.interventions.add(intervention);
        intervention.setDossier(this);
        return this;
    }

    public Dossier removeIntervention(Intervention intervention) {
        this.interventions.remove(intervention);
        intervention.setDossier(null);
        return this;
    }

    public void setInterventions(Set<Intervention> interventions) {
        this.interventions = interventions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dossier)) {
            return false;
        }
        return id != null && id.equals(((Dossier) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dossier{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", etat='" + getEtat() + "'" +
            ", urlAzimut='" + getUrlAzimut() + "'" +
            ", urlRedmine='" + getUrlRedmine() + "'" +
            ", urlAkuiteo='" + getUrlAkuiteo() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
