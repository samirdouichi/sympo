package com.aractronic.sympo.repository;

import com.aractronic.sympo.domain.CorrespondanceDocument;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CorrespondanceDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorrespondanceDocumentRepository extends JpaRepository<CorrespondanceDocument, Long> {

}
