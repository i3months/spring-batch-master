package io.springbatch.spring_batch_master.flow;

import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.context.annotation.Bean;

public class SimpleFlowConfig {

    @Bean
    public Job job(JobRepository jobRepository) {
        return new JobBuilder("helloJob", jobRepository)
            .start(flow())
            .next(step3())
            .end()
            .build();
    }
    
    @Bean
    public Flow flow() {
        FlowBuilder<Flow> builder = new FlowBuilder<>("flow");
        builder.start(step1()) 
                .next(step2())
                .end();

        builder.build();
    }

    
}
