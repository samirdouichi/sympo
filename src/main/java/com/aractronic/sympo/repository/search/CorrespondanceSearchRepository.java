package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.Correspondance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Correspondance entity.
 */
public interface CorrespondanceSearchRepository extends ElasticsearchRepository<Correspondance, Long> {
}
