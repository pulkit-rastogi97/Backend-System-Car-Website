package com.udacity.pricing.configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

/**
 * To populate database table from prices.json file under resources folder
 */
@Configuration
public class RepositoryConfiguration {

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean populateRepository(){
        Jackson2RepositoryPopulatorFactoryBean jackson2RepositoryPopulatorFactoryBean = new Jackson2RepositoryPopulatorFactoryBean();
        jackson2RepositoryPopulatorFactoryBean.setResources(new Resource[] { new ClassPathResource("prices.json") });
        return jackson2RepositoryPopulatorFactoryBean;
    }
}

