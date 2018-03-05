package com.aractronic.sympo.repository;

import com.aractronic.sympo.domain.CorrespondanceType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CorrespondanceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorrespondanceTypeRepository extends JpaRepository<CorrespondanceType, Long> {

}
