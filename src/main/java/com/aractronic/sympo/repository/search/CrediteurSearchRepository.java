package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.Crediteur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Crediteur entity.
 */
public interface CrediteurSearchRepository extends ElasticsearchRepository<Crediteur, Long> {
}
