package com.aractronic.sympo.repository;

import com.aractronic.sympo.domain.DossierInstruction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DossierInstruction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierInstructionRepository extends JpaRepository<DossierInstruction, Long> {

}
