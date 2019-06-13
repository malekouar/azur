package azur.support.web.tool.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Serveur.
 */
@Entity
@Table(name = "serveur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "serveur")
public class Serveur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Size(max = 45)
    @Column(name = "serveur_type", length = 45)
    private String serveurType;

    @Size(max = 45)
    @Column(name = "serveur_nom", length = 45)
    private String serveurNom;

    @Size(max = 45)
    @Column(name = "serveur_ip", length = 45)
    private String serveurIp;

    @Size(max = 45)
    @Column(name = "login", length = 45)
    private String login;

    @Size(max = 45)
    @Column(name = "password", length = 45)
    private String password;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("serveurs")
    private Config config;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServeurType() {
        return serveurType;
    }

    public Serveur serveurType(String serveurType) {
        this.serveurType = serveurType;
        return this;
    }

    public void setServeurType(String serveurType) {
        this.serveurType = serveurType;
    }

    public String getServeurNom() {
        return serveurNom;
    }

    public Serveur serveurNom(String serveurNom) {
        this.serveurNom = serveurNom;
        return this;
    }

    public void setServeurNom(String serveurNom) {
        this.serveurNom = serveurNom;
    }

    public String getServeurIp() {
        return serveurIp;
    }

    public Serveur serveurIp(String serveurIp) {
        this.serveurIp = serveurIp;
        return this;
    }

    public void setServeurIp(String serveurIp) {
        this.serveurIp = serveurIp;
    }

    public String getLogin() {
        return login;
    }

    public Serveur login(String login) {
        this.login = login;
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public Serveur password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Config getConfig() {
        return config;
    }

    public Serveur config(Config config) {
        this.config = config;
        return this;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Serveur)) {
            return false;
        }
        return id != null && id.equals(((Serveur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Serveur{" +
            "id=" + getId() +
            ", serveurType='" + getServeurType() + "'" +
            ", serveurNom='" + getServeurNom() + "'" +
            ", serveurIp='" + getServeurIp() + "'" +
            ", login='" + getLogin() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
