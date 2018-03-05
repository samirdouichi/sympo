package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.Debiteur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Debiteur entity.
 */
public interface DebiteurSearchRepository extends ElasticsearchRepository<Debiteur, Long> {
}
