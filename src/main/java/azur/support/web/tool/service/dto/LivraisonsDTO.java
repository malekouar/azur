package azur.support.web.tool.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link azur.support.web.tool.domain.Livraisons} entity.
 */
public class LivraisonsDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 45)
    private String type;

    @NotNull
    private Instant dateLivraison;

    @NotNull
    @Size(max = 45)
    private String responsable;

    @NotNull
    @Size(max = 45)
    private String etat;

    @NotNull
    @Size(max = 255)
    private String nomPackage;

    @NotNull
    private Integer idSvn;

    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String detail;


    private Long interventionId;

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

    public Instant getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Instant dateLivraison) {
        this.dateLivraison = dateLivraison;
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

    public String getNomPackage() {
        return nomPackage;
    }

    public void setNomPackage(String nomPackage) {
        this.nomPackage = nomPackage;
    }

    public Integer getIdSvn() {
        return idSvn;
    }

    public void setIdSvn(Integer idSvn) {
        this.idSvn = idSvn;
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

    public Long getInterventionId() {
        return interventionId;
    }

    public void setInterventionId(Long interventionId) {
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

        LivraisonsDTO livraisonsDTO = (LivraisonsDTO) o;
        if (livraisonsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), livraisonsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LivraisonsDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", dateLivraison='" + getDateLivraison() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", etat='" + getEtat() + "'" +
            ", nomPackage='" + getNomPackage() + "'" +
            ", idSvn=" + getIdSvn() +
            ", description='" + getDescription() + "'" +
            ", detail='" + getDetail() + "'" +
            ", intervention=" + getInterventionId() +
            "}";
    }
}
