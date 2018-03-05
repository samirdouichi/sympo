package com.aractronic.sympo.repository;

import com.aractronic.sympo.domain.Cabinet;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Cabinet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CabinetRepository extends JpaRepository<Cabinet, Long> {

}
