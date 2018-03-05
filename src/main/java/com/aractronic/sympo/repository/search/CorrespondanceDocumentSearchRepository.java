package com.aractronic.sympo.repository.search;

import com.aractronic.sympo.domain.CorrespondanceDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CorrespondanceDocument entity.
 */
public interface CorrespondanceDocumentSearchRepository extends ElasticsearchRepository<CorrespondanceDocument, Long> {
}
