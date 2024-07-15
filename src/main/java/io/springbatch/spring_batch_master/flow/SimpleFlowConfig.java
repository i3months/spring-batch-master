package io.springbatch.spring_batch_master.flow;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleFlowConfig {


    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public org.springframework.batch.core.Job job(JobRepository jobRepository) {
        return new JobBuilder("helloJob", jobRepository)
            .start(step1(jobRepository, platformTransactionManager))
            .on("COMPLETED").to(step2(jobRepository, platformTransactionManager))
            .from(step1(jobRepository, platformTransactionManager))            
            .on("FAILED").to(flow())
            .end()
            .build();
    }
    
    @Bean
    public Flow flow() {
        FlowBuilder<Flow> builder = new FlowBuilder<>("flow");
        builder.start(step2(jobRepository, platformTransactionManager)) 
                .on("*")
                .to(step3(jobRepository, platformTransactionManager))
                .end();

        return builder.build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("helloStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .build();
    }

    @Bean
    public Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("helloStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .build();
    }

    @Bean
    public Step step3(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("helloStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .build();
    }

    @Bean
    public Step step4(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("helloStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .build();
    }
}