package com.aractronic.sympo.repository;

import com.aractronic.sympo.domain.Debiteur;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Debiteur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DebiteurRepository extends JpaRepository<Debiteur, Long> {

}
