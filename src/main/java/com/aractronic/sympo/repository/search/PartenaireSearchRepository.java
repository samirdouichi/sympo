package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.Partenaire;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Partenaire entity.
 */
public interface PartenaireSearchRepository extends ElasticsearchRepository<Partenaire, Long> {
}
