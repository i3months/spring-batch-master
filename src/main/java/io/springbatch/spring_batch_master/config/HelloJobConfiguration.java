package io.springbatch.spring_batch_master.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class HelloJobConfiguration {

    @Bean
    public Job helloJob(JobRepository jobRepository) {
        return new JobBuilder("helloJob", jobRepository)
            .start(step(jobRepository, null))
            .build();
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("helloStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("Hello Step");
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .build();
    }
}