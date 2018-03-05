package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.CorrespondanceType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CorrespondanceType entity.
 */
public interface CorrespondanceTypeSearchRepository extends ElasticsearchRepository<CorrespondanceType, Long> {
}
