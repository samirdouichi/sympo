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
 * A CorrespondanceDocument.
 */
@Entity
@Table(name = "correspondance_document")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "correspondancedocument")
public class CorrespondanceDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "correspondance_document")
    private String correspondanceDocument;

    @Size(max = 100)
    @Column(name = "remarque", length = 100)
    private String remarque;

    @OneToOne(mappedBy = "correspondanceDocuments")
    @JsonIgnore
    private Correspondance custom;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorrespondanceDocument() {
        return correspondanceDocument;
    }

    public CorrespondanceDocument correspondanceDocument(String correspondanceDocument) {
        this.correspondanceDocument = correspondanceDocument;
        return this;
    }

    public void setCorrespondanceDocument(String correspondanceDocument) {
        this.correspondanceDocument = correspondanceDocument;
    }

    public String getRemarque() {
        return remarque;
    }

    public CorrespondanceDocument remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Correspondance getCustom() {
        return custom;
    }

    public CorrespondanceDocument custom(Correspondance correspondance) {
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
        CorrespondanceDocument correspondanceDocument = (CorrespondanceDocument) o;
        if (correspondanceDocument.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), correspondanceDocument.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CorrespondanceDocument{" +
            "id=" + getId() +
            ", correspondanceDocument='" + getCorrespondanceDocument() + "'" +
            ", remarque='" + getRemarque() + "'" +
            "}";
    }
}
