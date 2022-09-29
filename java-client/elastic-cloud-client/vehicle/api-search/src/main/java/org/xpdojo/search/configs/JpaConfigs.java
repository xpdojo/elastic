package org.xpdojo.search.configs;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.xpdojo.search.domain.CarSearchRepository;

@EnableJpaRepositories(
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = CarSearchRepository.class))
@Configuration
public class JpaConfigs {
}
