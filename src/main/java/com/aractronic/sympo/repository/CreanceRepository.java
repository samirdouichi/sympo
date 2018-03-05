package com.aractronic.sympo.repository;

import com.aractronic.sympo.domain.Creance;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Creance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CreanceRepository extends JpaRepository<Creance, Long> {

}
