package com.aractronic.sympo.repository;

import com.aractronic.sympo.domain.Crediteur;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Crediteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CrediteurRepository extends JpaRepository<Crediteur, Long> {

}
