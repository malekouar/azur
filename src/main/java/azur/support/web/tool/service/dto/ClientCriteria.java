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
 * Criteria class for the {@link azur.support.web.tool.domain.Client} entity. This class is used
 * in {@link azur.support.web.tool.web.rest.ClientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter raisonSociale;

    private StringFilter contact;

    private StringFilter tel;

    private StringFilter mobile;

    private StringFilter email;

    private LongFilter configId;

    private LongFilter dossierId;

    public ClientCriteria(){
    }

    public ClientCriteria(ClientCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.raisonSociale = other.raisonSociale == null ? null : other.raisonSociale.copy();
        this.contact = other.contact == null ? null : other.contact.copy();
        this.tel = other.tel == null ? null : other.tel.copy();
        this.mobile = other.mobile == null ? null : other.mobile.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.configId = other.configId == null ? null : other.configId.copy();
        this.dossierId = other.dossierId == null ? null : other.dossierId.copy();
    }

    @Override
    public ClientCriteria copy() {
        return new ClientCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(StringFilter raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public StringFilter getContact() {
        return contact;
    }

    public void setContact(StringFilter contact) {
        this.contact = contact;
    }

    public StringFilter getTel() {
        return tel;
    }

    public void setTel(StringFilter tel) {
        this.tel = tel;
    }

    public StringFilter getMobile() {
        return mobile;
    }

    public void setMobile(StringFilter mobile) {
        this.mobile = mobile;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getConfigId() {
        return configId;
    }

    public void setConfigId(LongFilter configId) {
        this.configId = configId;
    }

    public LongFilter getDossierId() {
        return dossierId;
    }

    public void setDossierId(LongFilter dossierId) {
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
        final ClientCriteria that = (ClientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(raisonSociale, that.raisonSociale) &&
            Objects.equals(contact, that.contact) &&
            Objects.equals(tel, that.tel) &&
            Objects.equals(mobile, that.mobile) &&
            Objects.equals(email, that.email) &&
            Objects.equals(configId, that.configId) &&
            Objects.equals(dossierId, that.dossierId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        raisonSociale,
        contact,
        tel,
        mobile,
        email,
        configId,
        dossierId
        );
    }

    @Override
    public String toString() {
        return "ClientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (raisonSociale != null ? "raisonSociale=" + raisonSociale + ", " : "") +
                (contact != null ? "contact=" + contact + ", " : "") +
                (tel != null ? "tel=" + tel + ", " : "") +
                (mobile != null ? "mobile=" + mobile + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (configId != null ? "configId=" + configId + ", " : "") +
                (dossierId != null ? "dossierId=" + dossierId + ", " : "") +
            "}";
    }

}
