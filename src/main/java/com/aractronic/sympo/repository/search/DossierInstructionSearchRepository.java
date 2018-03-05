package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.DossierInstruction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DossierInstruction entity.
 */
public interface DossierInstructionSearchRepository extends ElasticsearchRepository<DossierInstruction, Long> {
}
