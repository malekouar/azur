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
 * Criteria class for the {@link azur.support.web.tool.domain.Serveur} entity. This class is used
 * in {@link azur.support.web.tool.web.rest.ServeurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /serveurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ServeurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter serveurType;

    private StringFilter serveurNom;

    private StringFilter serveurIp;

    private StringFilter login;

    private StringFilter password;

    private LongFilter configId;

    public ServeurCriteria(){
    }

    public ServeurCriteria(ServeurCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.serveurType = other.serveurType == null ? null : other.serveurType.copy();
        this.serveurNom = other.serveurNom == null ? null : other.serveurNom.copy();
        this.serveurIp = other.serveurIp == null ? null : other.serveurIp.copy();
        this.login = other.login == null ? null : other.login.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.configId = other.configId == null ? null : other.configId.copy();
    }

    @Override
    public ServeurCriteria copy() {
        return new ServeurCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getServeurType() {
        return serveurType;
    }

    public void setServeurType(StringFilter serveurType) {
        this.serveurType = serveurType;
    }

    public StringFilter getServeurNom() {
        return serveurNom;
    }

    public void setServeurNom(StringFilter serveurNom) {
        this.serveurNom = serveurNom;
    }

    public StringFilter getServeurIp() {
        return serveurIp;
    }

    public void setServeurIp(StringFilter serveurIp) {
        this.serveurIp = serveurIp;
    }

    public StringFilter getLogin() {
        return login;
    }

    public void setLogin(StringFilter login) {
        this.login = login;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public LongFilter getConfigId() {
        return configId;
    }

    public void setConfigId(LongFilter configId) {
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
        final ServeurCriteria that = (ServeurCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(serveurType, that.serveurType) &&
            Objects.equals(serveurNom, that.serveurNom) &&
            Objects.equals(serveurIp, that.serveurIp) &&
            Objects.equals(login, that.login) &&
            Objects.equals(password, that.password) &&
            Objects.equals(configId, that.configId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        serveurType,
        serveurNom,
        serveurIp,
        login,
        password,
        configId
        );
    }

    @Override
    public String toString() {
        return "ServeurCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (serveurType != null ? "serveurType=" + serveurType + ", " : "") +
                (serveurNom != null ? "serveurNom=" + serveurNom + ", " : "") +
                (serveurIp != null ? "serveurIp=" + serveurIp + ", " : "") +
                (login != null ? "login=" + login + ", " : "") +
                (password != null ? "password=" + password + ", " : "") +
                (configId != null ? "configId=" + configId + ", " : "") +
            "}";
    }

}
