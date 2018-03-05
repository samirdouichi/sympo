package com.aractronic.sympo.repository;

import com.aractronic.sympo.domain.CategorieDossier;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CategorieDossier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieDossierRepository extends JpaRepository<CategorieDossier, Long> {

}
