package com.aractronic.sympo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CorrespondanceType.
 */
@Entity
@Table(name = "correspondance_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "correspondancetype")
public class CorrespondanceType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    @Column(name = "code_correspondance", length = 40)
    private String codeCorrespondance;

    @Size(max = 40)
    @Column(name = "nom_correspondance", length = 40)
    private String nomCorrespondance;

    @OneToOne(mappedBy = "correspondanceTypes")
    @JsonIgnore
    private Correspondance custom;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeCorrespondance() {
        return codeCorrespondance;
    }

    public CorrespondanceType codeCorrespondance(String codeCorrespondance) {
        this.codeCorrespondance = codeCorrespondance;
        return this;
    }

    public void setCodeCorrespondance(String codeCorrespondance) {
        this.codeCorrespondance = codeCorrespondance;
    }

    public String getNomCorrespondance() {
        return nomCorrespondance;
    }

    public CorrespondanceType nomCorrespondance(String nomCorrespondance) {
        this.nomCorrespondance = nomCorrespondance;
        return this;
    }

    public void setNomCorrespondance(String nomCorrespondance) {
        this.nomCorrespondance = nomCorrespondance;
    }

    public Correspondance getCustom() {
        return custom;
    }

    public CorrespondanceType custom(Correspondance correspondance) {
        this.custom = correspondance;
        return this;
    }

    public void setCustom(Correspondance correspondance) {
        this.custom = correspondance;
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
        CorrespondanceType correspondanceType = (CorrespondanceType) o;
        if (correspondanceType.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), correspondanceType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CorrespondanceType{" +
            "id=" + getId() +
            ", codeCorrespondance='" + getCodeCorrespondance() + "'" +
            ", nomCorrespondance='" + getNomCorrespondance() + "'" +
            "}";
    }
}
