package azur.support.web.tool.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Config.
 */
@Entity
@Table(name = "config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "config")
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Size(max = 45)
    @Column(name = "teamviewer_id", length = 45)
    private String teamviewerId;

    @Size(max = 45)
    @Column(name = "teamviewer_password", length = 45)
    private String teamviewerPassword;

    @Size(max = 45)
    @Column(name = "vpn_type", length = 45)
    private String vpnType;

    @Size(max = 45)
    @Column(name = "vpn_ip", length = 45)
    private String vpnIp;

    @Size(max = 255)
    @Column(name = "vpn_login", length = 255)
    private String vpnLogin;

    @Size(max = 100)
    @Column(name = "vpn_password", length = 100)
    private String vpnPassword;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("configs")
    private Client client;

    @OneToMany(mappedBy = "config")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Serveur> serveurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamviewerId() {
        return teamviewerId;
    }

    public Config teamviewerId(String teamviewerId) {
        this.teamviewerId = teamviewerId;
        return this;
    }

    public void setTeamviewerId(String teamviewerId) {
        this.teamviewerId = teamviewerId;
    }

    public String getTeamviewerPassword() {
        return teamviewerPassword;
    }

    public Config teamviewerPassword(String teamviewerPassword) {
        this.teamviewerPassword = teamviewerPassword;
        return this;
    }

    public void setTeamviewerPassword(String teamviewerPassword) {
        this.teamviewerPassword = teamviewerPassword;
    }

    public String getVpnType() {
        return vpnType;
    }

    public Config vpnType(String vpnType) {
        this.vpnType = vpnType;
        return this;
    }

    public void setVpnType(String vpnType) {
        this.vpnType = vpnType;
    }

    public String getVpnIp() {
        return vpnIp;
    }

    public Config vpnIp(String vpnIp) {
        this.vpnIp = vpnIp;
        return this;
    }

    public void setVpnIp(String vpnIp) {
        this.vpnIp = vpnIp;
    }

    public String getVpnLogin() {
        return vpnLogin;
    }

    public Config vpnLogin(String vpnLogin) {
        this.vpnLogin = vpnLogin;
        return this;
    }

    public void setVpnLogin(String vpnLogin) {
        this.vpnLogin = vpnLogin;
    }

    public String getVpnPassword() {
        return vpnPassword;
    }

    public Config vpnPassword(String vpnPassword) {
        this.vpnPassword = vpnPassword;
        return this;
    }

    public void setVpnPassword(String vpnPassword) {
        this.vpnPassword = vpnPassword;
    }

    public Client getClient() {
        return client;
    }

    public Config client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Serveur> getServeurs() {
        return serveurs;
    }

    public Config serveurs(Set<Serveur> serveurs) {
        this.serveurs = serveurs;
        return this;
    }

    public Config addServeur(Serveur serveur) {
        this.serveurs.add(serveur);
        serveur.setConfig(this);
        return this;
    }

    public Config removeServeur(Serveur serveur) {
        this.serveurs.remove(serveur);
        serveur.setConfig(null);
        return this;
    }

    public void setServeurs(Set<Serveur> serveurs) {
        this.serveurs = serveurs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Config)) {
            return false;
        }
        return id != null && id.equals(((Config) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Config{" +
            "id=" + getId() +
            ", teamviewerId='" + getTeamviewerId() + "'" +
            ", teamviewerPassword='" + getTeamviewerPassword() + "'" +
            ", vpnType='" + getVpnType() + "'" +
            ", vpnIp='" + getVpnIp() + "'" +
            ", vpnLogin='" + getVpnLogin() + "'" +
            ", vpnPassword='" + getVpnPassword() + "'" +
            "}";
    }
}
