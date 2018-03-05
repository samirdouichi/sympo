package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.Creance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Creance entity.
 */
public interface CreanceSearchRepository extends ElasticsearchRepository<Creance, Long> {
}
