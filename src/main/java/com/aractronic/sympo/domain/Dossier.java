package com.aractronic.sympo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.aractronic.sympo.domain.enumeration.EtatDossier;

/**
 * A Dossier.
 */
@Entity
@Table(name = "dossier")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "dossier")
public class Dossier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "num_dossier", length = 100, nullable = false)
    private String numDossier;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EtatDossier status;

    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "nom_dossier", length = 60, nullable = false)
    private String nomDossier;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    @Column(name = "date_cloture")
    private LocalDate dateCloture;

    @Size(max = 100)
    @Column(name = "remarque", length = 100)
    private String remarque;

    @OneToMany(mappedBy = "categorieDossier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CategorieDossier> dossierCategories = new HashSet<>();

    @OneToMany(mappedBy = "debiteur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Debiteur> dossierDebiteurs = new HashSet<>();

    @OneToMany(mappedBy = "partenaire")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Partenaire> dossierPartenaires = new HashSet<>();

    @OneToMany(mappedBy = "correspondance")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Correspondance> dossierCorrespondances = new HashSet<>();

    @OneToMany(mappedBy = "dossierInstruction")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DossierInstruction> dossierInstructions = new HashSet<>();

    @ManyToOne
    private Cabinet dossier;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumDossier() {
        return numDossier;
    }

    public Dossier numDossier(String numDossier) {
        this.numDossier = numDossier;
        return this;
    }

    public void setNumDossier(String numDossier) {
        this.numDossier = numDossier;
    }

    public EtatDossier getStatus() {
        return status;
    }

    public Dossier status(EtatDossier status) {
        this.status = status;
        return this;
    }

    public void setStatus(EtatDossier status) {
        this.status = status;
    }

    public String getNomDossier() {
        return nomDossier;
    }

    public Dossier nomDossier(String nomDossier) {
        this.nomDossier = nomDossier;
        return this;
    }

    public void setNomDossier(String nomDossier) {
        this.nomDossier = nomDossier;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public Dossier dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public Dossier updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getDateCloture() {
        return dateCloture;
    }

    public Dossier dateCloture(LocalDate dateCloture) {
        this.dateCloture = dateCloture;
        return this;
    }

    public void setDateCloture(LocalDate dateCloture) {
        this.dateCloture = dateCloture;
    }

    public String getRemarque() {
        return remarque;
    }

    public Dossier remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Set<CategorieDossier> getDossierCategories() {
        return dossierCategories;
    }

    public Dossier dossierCategories(Set<CategorieDossier> categorieDossiers) {
        this.dossierCategories = categorieDossiers;
        return this;
    }

    public Dossier addDossierCategories(CategorieDossier categorieDossier) {
        this.dossierCategories.add(categorieDossier);
        categorieDossier.setCategorieDossier(this);
        return this;
    }

    public Dossier removeDossierCategories(CategorieDossier categorieDossier) {
        this.dossierCategories.remove(categorieDossier);
        categorieDossier.setCategorieDossier(null);
        return this;
    }

    public void setDossierCategories(Set<CategorieDossier> categorieDossiers) {
        this.dossierCategories = categorieDossiers;
    }

    public Set<Debiteur> getDossierDebiteurs() {
        return dossierDebiteurs;
    }

    public Dossier dossierDebiteurs(Set<Debiteur> debiteurs) {
        this.dossierDebiteurs = debiteurs;
        return this;
    }

    public Dossier addDossierDebiteurs(Debiteur debiteur) {
        this.dossierDebiteurs.add(debiteur);
        debiteur.setDebiteur(this);
        return this;
    }

    public Dossier removeDossierDebiteurs(Debiteur debiteur) {
        this.dossierDebiteurs.remove(debiteur);
        debiteur.setDebiteur(null);
        return this;
    }

    public void setDossierDebiteurs(Set<Debiteur> debiteurs) {
        this.dossierDebiteurs = debiteurs;
    }

    public Set<Partenaire> getDossierPartenaires() {
        return dossierPartenaires;
    }

    public Dossier dossierPartenaires(Set<Partenaire> partenaires) {
        this.dossierPartenaires = partenaires;
        return this;
    }

    public Dossier addDossierPartenaires(Partenaire partenaire) {
        this.dossierPartenaires.add(partenaire);
        partenaire.setPartenaire(this);
        return this;
    }

    public Dossier removeDossierPartenaires(Partenaire partenaire) {
        this.dossierPartenaires.remove(partenaire);
        partenaire.setPartenaire(null);
        return this;
    }

    public void setDossierPartenaires(Set<Partenaire> partenaires) {
        this.dossierPartenaires = partenaires;
    }

    public Set<Correspondance> getDossierCorrespondances() {
        return dossierCorrespondances;
    }

    public Dossier dossierCorrespondances(Set<Correspondance> correspondances) {
        this.dossierCorrespondances = correspondances;
        return this;
    }

    public Dossier addDossierCorrespondances(Correspondance correspondance) {
        this.dossierCorrespondances.add(correspondance);
        correspondance.setCorrespondance(this);
        return this;
    }

    public Dossier removeDossierCorrespondances(Correspondance correspondance) {
        this.dossierCorrespondances.remove(correspondance);
        correspondance.setCorrespondance(null);
        return this;
    }

    public void setDossierCorrespondances(Set<Correspondance> correspondances) {
        this.dossierCorrespondances = correspondances;
    }

    public Set<DossierInstruction> getDossierInstructions() {
        return dossierInstructions;
    }

    public Dossier dossierInstructions(Set<DossierInstruction> dossierInstructions) {
        this.dossierInstructions = dossierInstructions;
        return this;
    }

    public Dossier addDossierInstructions(DossierInstruction dossierInstruction) {
        this.dossierInstructions.add(dossierInstruction);
        dossierInstruction.setDossierInstruction(this);
        return this;
    }

    public Dossier removeDossierInstructions(DossierInstruction dossierInstruction) {
        this.dossierInstructions.remove(dossierInstruction);
        dossierInstruction.setDossierInstruction(null);
        return this;
    }

    public void setDossierInstructions(Set<DossierInstruction> dossierInstructions) {
        this.dossierInstructions = dossierInstructions;
    }

    public Cabinet getDossier() {
        return dossier;
    }

    public Dossier dossier(Cabinet cabinet) {
        this.dossier = cabinet;
        return this;
    }

    public void setDossier(Cabinet cabinet) {
        this.dossier = cabinet;
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
        Dossier dossier = (Dossier) o;
        if (dossier.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dossier.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Dossier{" +
            "id=" + getId() +
            ", numDossier='" + getNumDossier() + "'" +
            ", status='" + getStatus() + "'" +
            ", nomDossier='" + getNomDossier() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", dateCloture='" + getDateCloture() + "'" +
            ", remarque='" + getRemarque() + "'" +
            "}";
    }
}
