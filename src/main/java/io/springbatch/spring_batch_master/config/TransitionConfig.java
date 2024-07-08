package io.springbatch.spring_batch_master.config;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TransitionConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    
    @Bean
    public Job batchJob() {
        return new JobBuilder("job", jobRepository)
            .start(step1())
                .on("FAILED")
                .to(step2())
                .on("FAILED")
                .stop()
            .from(step1())
                .on("*")
                .to(step3())
                .next(step4())
            .from(step2())
                .on("*")                
                .to(step5())
                .end()
            .build();
    }
}
