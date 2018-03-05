package com.aractronic.sympo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.aractronic.sympo.domain.enumeration.CreancesType;

/**
 * A Creance.
 */
@Entity
@Table(name = "creance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "creance")
public class Creance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "num_creance", length = 6, nullable = false)
    private String numCreance;

    @NotNull
    @Size(min = 1, max = 26)
    @Column(name = "nom_creance", length = 26, nullable = false)
    private String nomCreance;

    @Enumerated(EnumType.STRING)
    @Column(name = "creances_type")
    private CreancesType creancesType;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    @Column(name = "remarque")
    private String remarque;

    @ManyToOne
    private Crediteur creance;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumCreance() {
        return numCreance;
    }

    public Creance numCreance(String numCreance) {
        this.numCreance = numCreance;
        return this;
    }

    public void setNumCreance(String numCreance) {
        this.numCreance = numCreance;
    }

    public String getNomCreance() {
        return nomCreance;
    }

    public Creance nomCreance(String nomCreance) {
        this.nomCreance = nomCreance;
        return this;
    }

    public void setNomCreance(String nomCreance) {
        this.nomCreance = nomCreance;
    }

    public CreancesType getCreancesType() {
        return creancesType;
    }

    public Creance creancesType(CreancesType creancesType) {
        this.creancesType = creancesType;
        return this;
    }

    public void setCreancesType(CreancesType creancesType) {
        this.creancesType = creancesType;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Creance createdAt(LocalDate createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public Creance updatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRemarque() {
        return remarque;
    }

    public Creance remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Crediteur getCreance() {
        return creance;
    }

    public Creance creance(Crediteur crediteur) {
        this.creance = crediteur;
        return this;
    }

    public void setCreance(Crediteur crediteur) {
        this.creance = crediteur;
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
        Creance creance = (Creance) o;
        if (creance.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), creance.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Creance{" +
            "id=" + getId() +
            ", numCreance='" + getNumCreance() + "'" +
            ", nomCreance='" + getNomCreance() + "'" +
            ", creancesType='" + getCreancesType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", remarque='" + getRemarque() + "'" +
            "}";
    }
}
