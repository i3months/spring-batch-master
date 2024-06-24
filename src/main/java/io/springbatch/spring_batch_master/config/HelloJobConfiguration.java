package io.springbatch.spring_batch_master.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class HelloJobConfiguration {
    
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;


    @Bean
    public Job helloJob() {
        return new JobBuilder("helloJob", jobRepository)
            .start(step())
            .build();
    }

    @Bean
    public Step step() {
        return new StepBuilder("helloStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("Hello Step");
                return RepeatStatus.FINISHED;
            }, platformTransactionManager)
            .build();
                   
    }
    
}
