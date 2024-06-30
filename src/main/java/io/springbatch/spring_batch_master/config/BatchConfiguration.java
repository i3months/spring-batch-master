package io.springbatch.spring_batch_master.config;

import javax.sql.DataSource;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {
    
    @Bean
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager platformTransactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        
        factory.setDataSource(dataSource);
        factory.setTransactionManager(platformTransactionManager);
        factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
        factory.setTablePrefix("BATCH_");

        return factory.getObject();
    }
}
