package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.Cabinet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Cabinet entity.
 */
public interface CabinetSearchRepository extends ElasticsearchRepository<Cabinet, Long> {
}
