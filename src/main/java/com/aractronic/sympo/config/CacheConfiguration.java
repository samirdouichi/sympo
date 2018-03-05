package com.aractronic.sympo.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.aractronic.sympo.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Cabinet.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Cabinet.class.getName() + ".cabinets", jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Dossier.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Dossier.class.getName() + ".dossierCategories", jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Dossier.class.getName() + ".dossierDebiteurs", jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Dossier.class.getName() + ".dossierPartenaires", jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Dossier.class.getName() + ".dossierCorrespondances", jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Dossier.class.getName() + ".dossierInstructions", jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.DossierInstruction.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.CategorieDossier.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Correspondance.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.CorrespondanceDocument.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.CorrespondanceType.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Debiteur.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Crediteur.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Crediteur.class.getName() + ".crediteurCreances", jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Creance.class.getName(), jcacheConfiguration);
            cm.createCache(com.aractronic.sympo.domain.Partenaire.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
