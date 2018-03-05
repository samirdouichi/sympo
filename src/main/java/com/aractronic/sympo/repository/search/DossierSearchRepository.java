package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.Dossier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Dossier entity.
 */
public interface DossierSearchRepository extends ElasticsearchRepository<Dossier, Long> {
}
