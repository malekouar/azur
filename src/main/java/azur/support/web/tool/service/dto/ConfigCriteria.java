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

/**
 * Criteria class for the {@link azur.support.web.tool.domain.Config} entity. This class is used
 * in {@link azur.support.web.tool.web.rest.ConfigResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /configs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ConfigCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter teamviewerId;

    private StringFilter teamviewerPassword;

    private StringFilter vpnType;

    private StringFilter vpnIp;

    private StringFilter vpnLogin;

    private StringFilter vpnPassword;

    private LongFilter clientId;

    private LongFilter serveurId;

    public ConfigCriteria(){
    }

    public ConfigCriteria(ConfigCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.teamviewerId = other.teamviewerId == null ? null : other.teamviewerId.copy();
        this.teamviewerPassword = other.teamviewerPassword == null ? null : other.teamviewerPassword.copy();
        this.vpnType = other.vpnType == null ? null : other.vpnType.copy();
        this.vpnIp = other.vpnIp == null ? null : other.vpnIp.copy();
        this.vpnLogin = other.vpnLogin == null ? null : other.vpnLogin.copy();
        this.vpnPassword = other.vpnPassword == null ? null : other.vpnPassword.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
        this.serveurId = other.serveurId == null ? null : other.serveurId.copy();
    }

    @Override
    public ConfigCriteria copy() {
        return new ConfigCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTeamviewerId() {
        return teamviewerId;
    }

    public void setTeamviewerId(StringFilter teamviewerId) {
        this.teamviewerId = teamviewerId;
    }

    public StringFilter getTeamviewerPassword() {
        return teamviewerPassword;
    }

    public void setTeamviewerPassword(StringFilter teamviewerPassword) {
        this.teamviewerPassword = teamviewerPassword;
    }

    public StringFilter getVpnType() {
        return vpnType;
    }

    public void setVpnType(StringFilter vpnType) {
        this.vpnType = vpnType;
    }

    public StringFilter getVpnIp() {
        return vpnIp;
    }

    public void setVpnIp(StringFilter vpnIp) {
        this.vpnIp = vpnIp;
    }

    public StringFilter getVpnLogin() {
        return vpnLogin;
    }

    public void setVpnLogin(StringFilter vpnLogin) {
        this.vpnLogin = vpnLogin;
    }

    public StringFilter getVpnPassword() {
        return vpnPassword;
    }

    public void setVpnPassword(StringFilter vpnPassword) {
        this.vpnPassword = vpnPassword;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getServeurId() {
        return serveurId;
    }

    public void setServeurId(LongFilter serveurId) {
        this.serveurId = serveurId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ConfigCriteria that = (ConfigCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(teamviewerId, that.teamviewerId) &&
            Objects.equals(teamviewerPassword, that.teamviewerPassword) &&
            Objects.equals(vpnType, that.vpnType) &&
            Objects.equals(vpnIp, that.vpnIp) &&
            Objects.equals(vpnLogin, that.vpnLogin) &&
            Objects.equals(vpnPassword, that.vpnPassword) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(serveurId, that.serveurId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        teamviewerId,
        teamviewerPassword,
        vpnType,
        vpnIp,
        vpnLogin,
        vpnPassword,
        clientId,
        serveurId
        );
    }

    @Override
    public String toString() {
        return "ConfigCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (teamviewerId != null ? "teamviewerId=" + teamviewerId + ", " : "") +
                (teamviewerPassword != null ? "teamviewerPassword=" + teamviewerPassword + ", " : "") +
                (vpnType != null ? "vpnType=" + vpnType + ", " : "") +
                (vpnIp != null ? "vpnIp=" + vpnIp + ", " : "") +
                (vpnLogin != null ? "vpnLogin=" + vpnLogin + ", " : "") +
                (vpnPassword != null ? "vpnPassword=" + vpnPassword + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
                (serveurId != null ? "serveurId=" + serveurId + ", " : "") +
            "}";
    }

}
