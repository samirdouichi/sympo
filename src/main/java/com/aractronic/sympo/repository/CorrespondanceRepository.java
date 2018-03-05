package com.aractronic.sympo.repository;

import com.aractronic.sympo.domain.Correspondance;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Correspondance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorrespondanceRepository extends JpaRepository<Correspondance, Long> {

}
