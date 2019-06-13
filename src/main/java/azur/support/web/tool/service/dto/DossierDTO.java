package azur.support.web.tool.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link azur.support.web.tool.domain.Dossier} entity.
 */
public class DossierDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 45)
    private String type;

    @NotNull
    private Instant dateDebut;

    @NotNull
    @Size(max = 45)
    private String responsable;

    @NotNull
    @Size(max = 45)
    private String etat;

    @Size(max = 255)
    private String urlAzimut;

    @Size(max = 255)
    private String urlRedmine;

    @Size(max = 255)
    private String urlAkuiteo;

    @NotNull
    private Instant dateFin;


    private Long clientId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getUrlAzimut() {
        return urlAzimut;
    }

    public void setUrlAzimut(String urlAzimut) {
        this.urlAzimut = urlAzimut;
    }

    public String getUrlRedmine() {
        return urlRedmine;
    }

    public void setUrlRedmine(String urlRedmine) {
        this.urlRedmine = urlRedmine;
    }

    public String getUrlAkuiteo() {
        return urlAkuiteo;
    }

    public void setUrlAkuiteo(String urlAkuiteo) {
        this.urlAkuiteo = urlAkuiteo;
    }

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DossierDTO dossierDTO = (DossierDTO) o;
        if (dossierDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dossierDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DossierDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", etat='" + getEtat() + "'" +
            ", urlAzimut='" + getUrlAzimut() + "'" +
            ", urlRedmine='" + getUrlRedmine() + "'" +
            ", urlAkuiteo='" + getUrlAkuiteo() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", client=" + getClientId() +
            "}";
    }
}
