package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.CategorieDossier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CategorieDossier entity.
 */
public interface CategorieDossierSearchRepository extends ElasticsearchRepository<CategorieDossier, Long> {
}
