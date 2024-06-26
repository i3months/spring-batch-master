package io.springbatch.spring_batch_master.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JobConfiguration {
    
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job job() {
        return new JobBuilder("job", jobRepository)
        .start(step1())
        .next(step2())
        .build();
    }


    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("ddddd");
                return RepeatStatus.FINISHED;
            }, platformTransactionManager)
            .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step1", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("dddddddd");
                return RepeatStatus.FINISHED;
            }, platformTransactionManager)
            .build();
    }

}
