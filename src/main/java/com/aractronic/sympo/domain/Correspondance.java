package com.aractronic.sympo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.aractronic.sympo.domain.enumeration.EtatCorrespondance;

/**
 * A Correspondance.
 */
@Entity
@Table(name = "correspondance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "correspondance")
public class Correspondance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "correspondance_nom", length = 15, nullable = false)
    private String correspondanceNom;

    @Enumerated(EnumType.STRING)
    @Column(name = "etat_correspondance")
    private EtatCorrespondance etatCorrespondance;

    @Size(max = 100)
    @Column(name = "remarque", length = 100)
    private String remarque;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    @OneToOne
    @JoinColumn(unique = true)
    private CorrespondanceType correspondanceTypes;

    @OneToOne
    @JoinColumn(unique = true)
    private CorrespondanceDocument correspondanceDocuments;

    @ManyToOne
    private Dossier correspondance;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorrespondanceNom() {
        return correspondanceNom;
    }

    public Correspondance correspondanceNom(String correspondanceNom) {
        this.correspondanceNom = correspondanceNom;
        return this;
    }

    public void setCorrespondanceNom(String correspondanceNom) {
        this.correspondanceNom = correspondanceNom;
    }

    public EtatCorrespondance getEtatCorrespondance() {
        return etatCorrespondance;
    }

    public Correspondance etatCorrespondance(EtatCorrespondance etatCorrespondance) {
        this.etatCorrespondance = etatCorrespondance;
        return this;
    }

    public void setEtatCorrespondance(EtatCorrespondance etatCorrespondance) {
        this.etatCorrespondance = etatCorrespondance;
    }

    public String getRemarque() {
        return remarque;
    }

    public Correspondance remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Correspondance createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public Correspondance updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public CorrespondanceType getCorrespondanceTypes() {
        return correspondanceTypes;
    }

    public Correspondance correspondanceTypes(CorrespondanceType correspondanceType) {
        this.correspondanceTypes = correspondanceType;
        return this;
    }

    public void setCorrespondanceTypes(CorrespondanceType correspondanceType) {
        this.correspondanceTypes = correspondanceType;
    }

    public CorrespondanceDocument getCorrespondanceDocuments() {
        return correspondanceDocuments;
    }

    public Correspondance correspondanceDocuments(CorrespondanceDocument correspondanceDocument) {
        this.correspondanceDocuments = correspondanceDocument;
        return this;
    }

    public void setCorrespondanceDocuments(CorrespondanceDocument correspondanceDocument) {
        this.correspondanceDocuments = correspondanceDocument;
    }

    public Dossier getCorrespondance() {
        return correspondance;
    }

    public Correspondance correspondance(Dossier dossier) {
        this.correspondance = dossier;
        return this;
    }

    public void setCorrespondance(Dossier dossier) {
        this.correspondance = dossier;
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
        Correspondance correspondance = (Correspondance) o;
        if (correspondance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), correspondance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Correspondance{" +
            "id=" + getId() +
            ", correspondanceNom='" + getCorrespondanceNom() + "'" +
            ", etatCorrespondance='" + getEtatCorrespondance() + "'" +
            ", remarque='" + getRemarque() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
