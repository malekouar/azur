package azur.support.web.tool.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link azur.support.web.tool.domain.Config} entity.
 */
public class ConfigDTO implements Serializable {

    private Long id;

    @Size(max = 45)
    private String teamviewerId;

    @Size(max = 45)
    private String teamviewerPassword;

    @Size(max = 45)
    private String vpnType;

    @Size(max = 45)
    private String vpnIp;

    @Size(max = 255)
    private String vpnLogin;

    @Size(max = 100)
    private String vpnPassword;


    private Long clientId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamviewerId() {
        return teamviewerId;
    }

    public void setTeamviewerId(String teamviewerId) {
        this.teamviewerId = teamviewerId;
    }

    public String getTeamviewerPassword() {
        return teamviewerPassword;
    }

    public void setTeamviewerPassword(String teamviewerPassword) {
        this.teamviewerPassword = teamviewerPassword;
    }

    public String getVpnType() {
        return vpnType;
    }

    public void setVpnType(String vpnType) {
        this.vpnType = vpnType;
    }

    public String getVpnIp() {
        return vpnIp;
    }

    public void setVpnIp(String vpnIp) {
        this.vpnIp = vpnIp;
    }

    public String getVpnLogin() {
        return vpnLogin;
    }

    public void setVpnLogin(String vpnLogin) {
        this.vpnLogin = vpnLogin;
    }

    public String getVpnPassword() {
        return vpnPassword;
    }

    public void setVpnPassword(String vpnPassword) {
        this.vpnPassword = vpnPassword;
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

        ConfigDTO configDTO = (ConfigDTO) o;
        if (configDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), configDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ConfigDTO{" +
            "id=" + getId() +
            ", teamviewerId='" + getTeamviewerId() + "'" +
            ", teamviewerPassword='" + getTeamviewerPassword() + "'" +
            ", vpnType='" + getVpnType() + "'" +
            ", vpnIp='" + getVpnIp() + "'" +
            ", vpnLogin='" + getVpnLogin() + "'" +
            ", vpnPassword='" + getVpnPassword() + "'" +
            ", client=" + getClientId() +
            "}";
    }
}
