package com.aractronic.sympo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Crediteur.
 */
@Entity
@Table(name = "crediteur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "crediteur")
public class Crediteur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    @Column(name = "code_crediteur", length = 40)
    private String codeCrediteur;

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
    @Column(name = "nom_crediteur", length = 40)
    private String nomCrediteur;

    @Size(max = 40)
    @Column(name = "prenom_crediteur", length = 40)
    private String prenomCrediteur;

    @NotNull
    @Column(name = "activated", nullable = false)
    private Boolean activated;

    @Size(max = 80)
    @Column(name = "remarque", length = 80)
    private String remarque;

    @OneToOne
    @JoinColumn(unique = true)
    private DossierInstruction crediteur;

    @OneToMany(mappedBy = "creance")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Creance> crediteurCreances = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeCrediteur() {
        return codeCrediteur;
    }

    public Crediteur codeCrediteur(String codeCrediteur) {
        this.codeCrediteur = codeCrediteur;
        return this;
    }

    public void setCodeCrediteur(String codeCrediteur) {
        this.codeCrediteur = codeCrediteur;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public Crediteur raisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
        return this;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public String getRaisonSocialArabe() {
        return raisonSocialArabe;
    }

    public Crediteur raisonSocialArabe(String raisonSocialArabe) {
        this.raisonSocialArabe = raisonSocialArabe;
        return this;
    }

    public void setRaisonSocialArabe(String raisonSocialArabe) {
        this.raisonSocialArabe = raisonSocialArabe;
    }

    public String getRc() {
        return rc;
    }

    public Crediteur rc(String rc) {
        this.rc = rc;
        return this;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getPatente() {
        return patente;
    }

    public Crediteur patente(String patente) {
        this.patente = patente;
        return this;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public Crediteur numTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
        return this;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public String getEmail() {
        return email;
    }

    public Crediteur email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public Crediteur adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getQuartier() {
        return quartier;
    }

    public Crediteur quartier(String quartier) {
        this.quartier = quartier;
        return this;
    }

    public void setQuartier(String quartier) {
        this.quartier = quartier;
    }

    public String getVille() {
        return ville;
    }

    public Crediteur ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getFax() {
        return fax;
    }

    public Crediteur fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNomCrediteur() {
        return nomCrediteur;
    }

    public Crediteur nomCrediteur(String nomCrediteur) {
        this.nomCrediteur = nomCrediteur;
        return this;
    }

    public void setNomCrediteur(String nomCrediteur) {
        this.nomCrediteur = nomCrediteur;
    }

    public String getPrenomCrediteur() {
        return prenomCrediteur;
    }

    public Crediteur prenomCrediteur(String prenomCrediteur) {
        this.prenomCrediteur = prenomCrediteur;
        return this;
    }

    public void setPrenomCrediteur(String prenomCrediteur) {
        this.prenomCrediteur = prenomCrediteur;
    }

    public Boolean isActivated() {
        return activated;
    }

    public Crediteur activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getRemarque() {
        return remarque;
    }

    public Crediteur remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public DossierInstruction getCrediteur() {
        return crediteur;
    }

    public Crediteur crediteur(DossierInstruction dossierInstruction) {
        this.crediteur = dossierInstruction;
        return this;
    }

    public void setCrediteur(DossierInstruction dossierInstruction) {
        this.crediteur = dossierInstruction;
    }

    public Set<Creance> getCrediteurCreances() {
        return crediteurCreances;
    }

    public Crediteur crediteurCreances(Set<Creance> creances) {
        this.crediteurCreances = creances;
        return this;
    }

    public Crediteur addCrediteurCreance(Creance creance) {
        this.crediteurCreances.add(creance);
        creance.setCreance(this);
        return this;
    }

    public Crediteur removeCrediteurCreance(Creance creance) {
        this.crediteurCreances.remove(creance);
        creance.setCreance(null);
        return this;
    }

    public void setCrediteurCreances(Set<Creance> creances) {
        this.crediteurCreances = creances;
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
        Crediteur crediteur = (Crediteur) o;
        if (crediteur.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), crediteur.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Crediteur{" +
            "id=" + getId() +
            ", codeCrediteur='" + getCodeCrediteur() + "'" +
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
            ", nomCrediteur='" + getNomCrediteur() + "'" +
            ", prenomCrediteur='" + getPrenomCrediteur() + "'" +
            ", activated='" + isActivated() + "'" +
            ", remarque='" + getRemarque() + "'" +
            "}";
    }
}
