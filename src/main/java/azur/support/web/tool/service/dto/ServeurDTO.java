package azur.support.web.tool.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link azur.support.web.tool.domain.Serveur} entity.
 */
public class ServeurDTO implements Serializable {

    private Long id;

    @Size(max = 45)
    private String serveurType;

    @Size(max = 45)
    private String serveurNom;

    @Size(max = 45)
    private String serveurIp;

    @Size(max = 45)
    private String login;

    @Size(max = 45)
    private String password;


    private Long configId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServeurType() {
        return serveurType;
    }

    public void setServeurType(String serveurType) {
        this.serveurType = serveurType;
    }

    public String getServeurNom() {
        return serveurNom;
    }

    public void setServeurNom(String serveurNom) {
        this.serveurNom = serveurNom;
    }

    public String getServeurIp() {
        return serveurIp;
    }

    public void setServeurIp(String serveurIp) {
        this.serveurIp = serveurIp;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServeurDTO serveurDTO = (ServeurDTO) o;
        if (serveurDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serveurDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServeurDTO{" +
            "id=" + getId() +
            ", serveurType='" + getServeurType() + "'" +
            ", serveurNom='" + getServeurNom() + "'" +
            ", serveurIp='" + getServeurIp() + "'" +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            ", config=" + getConfigId() +
            "}";
    }
}
