package com.aractronic.sympo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DossierInstruction.
 */
@Entity
@Table(name = "dossier_instruction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "dossierinstruction")
public class DossierInstruction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "num_instruction", length = 6, nullable = false)
    private String numInstruction;

    @Size(max = 100)
    @Column(name = "instruction", length = 100)
    private String instruction;

    @Size(max = 100)
    @Column(name = "support_instruction", length = 100)
    private String supportInstruction;

    @Column(name = "date_creation_instruction")
    private LocalDate dateCreationInstruction;

    @Size(max = 100)
    @Column(name = "remarque", length = 100)
    private String remarque;

    @OneToOne(mappedBy = "crediteur")
    @JsonIgnore
    private Crediteur custom;

    @ManyToOne
    private Dossier dossierInstruction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumInstruction() {
        return numInstruction;
    }

    public DossierInstruction numInstruction(String numInstruction) {
        this.numInstruction = numInstruction;
        return this;
    }

    public void setNumInstruction(String numInstruction) {
        this.numInstruction = numInstruction;
    }

    public String getInstruction() {
        return instruction;
    }

    public DossierInstruction instruction(String instruction) {
        this.instruction = instruction;
        return this;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getSupportInstruction() {
        return supportInstruction;
    }

    public DossierInstruction supportInstruction(String supportInstruction) {
        this.supportInstruction = supportInstruction;
        return this;
    }

    public void setSupportInstruction(String supportInstruction) {
        this.supportInstruction = supportInstruction;
    }

    public LocalDate getDateCreationInstruction() {
        return dateCreationInstruction;
    }

    public DossierInstruction dateCreationInstruction(LocalDate dateCreationInstruction) {
        this.dateCreationInstruction = dateCreationInstruction;
        return this;
    }

    public void setDateCreationInstruction(LocalDate dateCreationInstruction) {
        this.dateCreationInstruction = dateCreationInstruction;
    }

    public String getRemarque() {
        return remarque;
    }

    public DossierInstruction remarque(String remarque) {
        this.remarque = remarque;
        return this;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public Crediteur getCustom() {
        return custom;
    }

    public DossierInstruction custom(Crediteur crediteur) {
        this.custom = crediteur;
        return this;
    }

    public void setCustom(Crediteur crediteur) {
        this.custom = crediteur;
    }

    public Dossier getDossierInstruction() {
        return dossierInstruction;
    }

    public DossierInstruction dossierInstruction(Dossier dossier) {
        this.dossierInstruction = dossier;
        return this;
    }

    public void setDossierInstruction(Dossier dossier) {
        this.dossierInstruction = dossier;
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
        DossierInstruction dossierInstruction = (DossierInstruction) o;
        if (dossierInstruction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dossierInstruction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DossierInstruction{" +
            "id=" + getId() +
            ", numInstruction='" + getNumInstruction() + "'" +
            ", instruction='" + getInstruction() + "'" +
            ", supportInstruction='" + getSupportInstruction() + "'" +
            ", dateCreationInstruction='" + getDateCreationInstruction() + "'" +
            ", remarque='" + getRemarque() + "'" +
            "}";
    }
}
