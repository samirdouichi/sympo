package com.aractronic.sympo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Debiteur.
 */
@Entity
@Table(name = "debiteur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "debiteur")
public class Debiteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    @Column(name = "code_debiteur", length = 40)
    private String codeDebiteur;

    @Size(max = 40)
    @Column(name = "raison_social", length = 40)
    private String raisonSocial;

    @Size(max = 40)
    @Column(name = "raison_social_arabe", length = 40)
    private String raisonSocialArabe;

    @Size(max = 20)
    @Column(name = "rc", length = 20)
    private String rc;

    @Size(max = 20)
    @Column(name = "patente", length = 20)
    private String patente;

    @Size(max = 40)
    @Column(name = "num_telephone", length = 40)
    private String numTelephone;

    @Size(min = 5, max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 40)
    @Column(name = "adresse", length = 40)
    private String adresse;

    @Size(max = 40)
    @Column(name = "quartier", length = 40)
    private String quartier;

    @Size(max = 40)
    @Column(name = "ville", length = 40)
    private String ville;

    @Size(max = 40)
    @Column(name = "fax", length = 40)
    private String fax;

    @Size(max = 40)
    @Column(name = "nom_debiteur", length = 40)
    private String nomDebiteur;

    @Size(max = 40)
    @Column(name = "cin", length = 40)
    private String cin;

    @Size(max = 40)
    @Column(name = "profession", length = 40)
    private String profession;

    @NotNull
    @Column(name = "activated", nullable = false)
    private Boolean activated;

    @Size(max = 80)
    @Column(name = "remarque", length = 80)
    private String remarque;

    @ManyToOne
    private Dossier debiteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeDebiteur() {
        return codeDebiteur;
    }

    public Debiteur codeDebiteur(String codeDebiteur) {
        this.codeDebiteur = codeDebiteur;
        return this;
    }

    public void setCodeDebiteur(String codeDebiteur) {
        this.codeDebiteur = codeDebiteur;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public Debiteur raisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
        return this;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public String getRaisonSocialArabe() {
        return raisonSocialArabe;
    }

    public Debiteur raisonSocialArabe(String raisonSocialArabe) {
        this.raisonSocialArabe = raisonSocialArabe;
        return this;
    }

    public void setRaisonSocialArabe(String raisonSocialArabe) {
        this.raisonSocialArabe = raisonSocialArabe;
    }

    public String getRc() {
        return rc;
    }

    public Debiteur rc(String rc) {
        this.rc = rc;
        return this;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getPatente() {
        return patente;
    }

    public Debiteur patente(String patente) {
        this.patente = patente;
        return this;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public Debiteur numTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
        return this;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public String getEmail() {
        return email;
    }

    public Debiteur email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public Debiteur adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getQuartier() {
        return quartier;
    }

    public Debiteur quartier(String quartier) {
        this.quartier = quartier;
        return this;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public String getVille() {
        return ville;
    }

    public Debiteur ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getFax() {
        return fax;
    }

    public Debiteur fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNomDebiteur() {
        return nomDebiteur;
    }

    public Debiteur nomDebiteur(String nomDebiteur) {
        this.nomDebiteur = nomDebiteur;
        return this;
    }

    public void setNomDebiteur(String nomDebiteur) {
        this.nomDebiteur = nomDebiteur;
    }

    public String getCin() {
        return cin;
    }

    public Debiteur cin(String cin) {
        this.cin = cin;
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getProfession() {
        return profession;
    }

    public Debiteur profession(String profession) {
        this.profession = profession;
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Boolean isActivated() {
        return activated;
    }

    public Debiteur activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getRemarque() {
        return remarque;
    }

    public Debiteur remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Dossier getDebiteur() {
        return debiteur;
    }

    public Debiteur debiteur(Dossier dossier) {
        this.debiteur = dossier;
        return this;
    }

    public void setDebiteur(Dossier dossier) {
        this.debiteur = dossier;
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
        Debiteur debiteur = (Debiteur) o;
        if (debiteur.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), debiteur.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Debiteur{" +
            "id=" + getId() +
            ", codeDebiteur='" + getCodeDebiteur() + "'" +
            ", raisonSocial='" + getRaisonSocial() + "'" +
            ", raisonSocialArabe='" + getRaisonSocialArabe() + "'" +
            ", rc='" + getRc() + "'" +
            ", patente='" + getPatente() + "'" +
            ", numTelephone='" + getNumTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", quartier='" + getQuartier() + "'" +
            ", ville='" + getVille() + "'" +
            ", fax='" + getFax() + "'" +
            ", nomDebiteur='" + getNomDebiteur() + "'" +
            ", cin='" + getCin() + "'" +
            ", profession='" + getProfession() + "'" +
            ", activated='" + isActivated() + "'" +
            ", remarque='" + getRemarque() + "'" +
            "}";
    }
}
