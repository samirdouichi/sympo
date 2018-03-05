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

/**
 * A Cabinet.
 */
@Entity
@Table(name = "cabinet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cabinet")
public class Cabinet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @Column(name = "nom_gerant", length = 40)
    private String nomGerant;

    @NotNull
    @Column(name = "activated", nullable = false)
    private Boolean activated;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @Size(max = 80)
    @Column(name = "remarque", length = 80)
    private String remarque;

    @OneToMany(mappedBy = "dossier")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dossier> cabinets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSocial() {
        return raisonSocial;
    }

    public Cabinet raisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
        return this;
    }

    public void setRaisonSocial(String raisonSocial) {
        this.raisonSocial = raisonSocial;
    }

    public String getRaisonSocialArabe() {
        return raisonSocialArabe;
    }

    public Cabinet raisonSocialArabe(String raisonSocialArabe) {
        this.raisonSocialArabe = raisonSocialArabe;
        return this;
    }

    public void setRaisonSocialArabe(String raisonSocialArabe) {
        this.raisonSocialArabe = raisonSocialArabe;
    }

    public String getRc() {
        return rc;
    }

    public Cabinet rc(String rc) {
        this.rc = rc;
        return this;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getPatente() {
        return patente;
    }

    public Cabinet patente(String patente) {
        this.patente = patente;
        return this;
    }

    public void setPatente(String patente) {
        this.patente = patente;
    }

    public String getNumTelephone() {
        return numTelephone;
    }

    public Cabinet numTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
        return this;
    }

    public void setNumTelephone(String numTelephone) {
        this.numTelephone = numTelephone;
    }

    public String getEmail() {
        return email;
    }

    public Cabinet email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public Cabinet adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNomGerant() {
        return nomGerant;
    }

    public Cabinet nomGerant(String nomGerant) {
        this.nomGerant = nomGerant;
        return this;
    }

    public void setNomGerant(String nomGerant) {
        this.nomGerant = nomGerant;
    }

    public Boolean isActivated() {
        return activated;
    }

    public Cabinet activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public Cabinet dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getRemarque() {
        return remarque;
    }

    public Cabinet remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Set<Dossier> getCabinets() {
        return cabinets;
    }

    public Cabinet cabinets(Set<Dossier> dossiers) {
        this.cabinets = dossiers;
        return this;
    }

    public Cabinet addCabinet(Dossier dossier) {
        this.cabinets.add(dossier);
        dossier.setDossier(this);
        return this;
    }

    public Cabinet removeCabinet(Dossier dossier) {
        this.cabinets.remove(dossier);
        dossier.setDossier(null);
        return this;
    }

    public void setCabinets(Set<Dossier> dossiers) {
        this.cabinets = dossiers;
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
        Cabinet cabinet = (Cabinet) o;
        if (cabinet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cabinet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Cabinet{" +
            "id=" + getId() +
            ", raisonSocial='" + getRaisonSocial() + "'" +
            ", raisonSocialArabe='" + getRaisonSocialArabe() + "'" +
            ", rc='" + getRc() + "'" +
            ", patente='" + getPatente() + "'" +
            ", numTelephone='" + getNumTelephone() + "'" +
            ", email='" + getEmail() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", nomGerant='" + getNomGerant() + "'" +
            ", activated='" + isActivated() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", remarque='" + getRemarque() + "'" +
            "}";
    }
}
