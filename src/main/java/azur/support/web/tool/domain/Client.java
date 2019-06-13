package azur.support.web.tool.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Client.
 */
@Entity
@Table(name = "client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "raison_sociale", length = 255, nullable = false)
    private String raisonSociale;

    @NotNull
    @Size(max = 45)
    @Column(name = "contact", length = 45, nullable = false)
    private String contact;

    @Size(max = 45)
    @Column(name = "tel", length = 45)
    private String tel;

    @Size(max = 45)
    @Column(name = "mobile", length = 45)
    private String mobile;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Config> configs = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dossier> dossiers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public Client raisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
        return this;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getContact() {
        return contact;
    }

    public Client contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTel() {
        return tel;
    }

    public Client tel(String tel) {
        this.tel = tel;
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public Client mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public Client email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Config> getConfigs() {
        return configs;
    }

    public Client configs(Set<Config> configs) {
        this.configs = configs;
        return this;
    }

    public Client addConfig(Config config) {
        this.configs.add(config);
        config.setClient(this);
        return this;
    }

    public Client removeConfig(Config config) {
        this.configs.remove(config);
        config.setClient(null);
        return this;
    }

    public void setConfigs(Set<Config> configs) {
        this.configs = configs;
    }

    public Set<Dossier> getDossiers() {
        return dossiers;
    }

    public Client dossiers(Set<Dossier> dossiers) {
        this.dossiers = dossiers;
        return this;
    }

    public Client addDossier(Dossier dossier) {
        this.dossiers.add(dossier);
        dossier.setClient(this);
        return this;
    }

    public Client removeDossier(Dossier dossier) {
        this.dossiers.remove(dossier);
        dossier.setClient(null);
        return this;
    }

    public void setDossiers(Set<Dossier> dossiers) {
        this.dossiers = dossiers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", raisonSociale='" + getRaisonSociale() + "'" +
            ", contact='" + getContact() + "'" +
            ", tel='" + getTel() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
