package com.aractronic.sympo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CategorieDossier.
 */
@Entity
@Table(name = "categorie_dossier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "categoriedossier")
public class CategorieDossier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "num_cat_dossier", length = 6, nullable = false)
    private String numCatDossier;

    @Size(min = 1, max = 6)
    @Column(name = "nom_cat_dossier", length = 6)
    private String nomCatDossier;

    @Size(max = 100)
    @Column(name = "remarque", length = 100)
    private String remarque;

    @ManyToOne
    private Dossier categorieDossier;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumCatDossier() {
        return numCatDossier;
    }

    public CategorieDossier numCatDossier(String numCatDossier) {
        this.numCatDossier = numCatDossier;
        return this;
    }

    public void setNumCatDossier(String numCatDossier) {
        this.numCatDossier = numCatDossier;
    }

    public String getNomCatDossier() {
        return nomCatDossier;
    }

    public CategorieDossier nomCatDossier(String nomCatDossier) {
        this.nomCatDossier = nomCatDossier;
        return this;
    }

    public void setNomCatDossier(String nomCatDossier) {
        this.nomCatDossier = nomCatDossier;
    }

    public String getRemarque() {
        return remarque;
    }

    public CategorieDossier remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Dossier getCategorieDossier() {
        return categorieDossier;
    }

    public CategorieDossier categorieDossier(Dossier dossier) {
        this.categorieDossier = dossier;
        return this;
    }

    public void setCategorieDossier(Dossier dossier) {
        this.categorieDossier = dossier;
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
        CategorieDossier categorieDossier = (CategorieDossier) o;
        if (categorieDossier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), categorieDossier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CategorieDossier{" +
            "id=" + getId() +
            ", numCatDossier='" + getNumCatDossier() + "'" +
            ", nomCatDossier='" + getNomCatDossier() + "'" +
            ", remarque='" + getRemarque() + "'" +
            "}";
    }
}
