package azur.support.web.tool.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link azur.support.web.tool.domain.Intervention} entity.
 */
public class InterventionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 45)
    private String type;

    @NotNull
    private Instant dateDebut;

    private Instant dateFin;

    @NotNull
    @Size(max = 45)
    private String responsable;

    @NotNull
    @Size(max = 45)
    private String etat;

    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String detail;


    private Long dossierId;

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

    public Instant getDateFin() {
        return dateFin;
    }

    public void setDateFin(Instant dateFin) {
        this.dateFin = dateFin;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getDossierId() {
        return dossierId;
    }

    public void setDossierId(Long dossierId) {
        this.dossierId = dossierId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InterventionDTO interventionDTO = (InterventionDTO) o;
        if (interventionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), interventionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InterventionDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", etat='" + getEtat() + "'" +
            ", description='" + getDescription() + "'" +
            ", detail='" + getDetail() + "'" +
            ", dossier=" + getDossierId() +
            "}";
    }
}
