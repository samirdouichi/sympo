package com.aractronic.sympo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Partenaire.
 */
@Entity
@Table(name = "partenaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "partenaire")
public class Partenaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "nom_partenaire", length = 10, nullable = false)
    private String nomPartenaire;

    @Column(name = "reset_date")
    private ZonedDateTime resetDate;

    @ManyToOne
    private Dossier partenaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPartenaire() {
        return nomPartenaire;
    }

    public Partenaire nomPartenaire(String nomPartenaire) {
        this.nomPartenaire = nomPartenaire;
        return this;
    }

    public void setNomPartenaire(String nomPartenaire) {
        this.nomPartenaire = nomPartenaire;
    }

    public ZonedDateTime getResetDate() {
        return resetDate;
    }

    public Partenaire resetDate(ZonedDateTime resetDate) {
        this.resetDate = resetDate;
        return this;
    }

    public void setResetDate(ZonedDateTime resetDate) {
        this.resetDate = resetDate;
    }

    public Dossier getPartenaire() {
        return partenaire;
    }

    public Partenaire partenaire(Dossier dossier) {
        this.partenaire = dossier;
        return this;
    }

    public void setPartenaire(Dossier dossier) {
        this.partenaire = dossier;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Partenaire partenaire = (Partenaire) o;
        if (partenaire.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), partenaire.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Partenaire{" +
            "id=" + getId() +
            ", nomPartenaire='" + getNomPartenaire() + "'" +
            ", resetDate='" + getResetDate() + "'" +
            "}";
    }
}
