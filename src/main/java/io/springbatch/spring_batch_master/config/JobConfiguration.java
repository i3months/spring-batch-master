package io.springbatch.spring_batch_master.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
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
            .incrementer(new RunIdIncrementer())
            .preventRestart()
            .validator(new CustomJobParameterValidator())
            .build();
    }

    @Bean
    public Flow flow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow"); // step = flow
        flowBuilder.start(step3())            
            .next(step4())
            .end();

        return flowBuilder.build();
    }


    @Bean
    public Step taskStep() {
        return new StepBuilder("taskStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("TaskStep");
                return RepeatStatus.FINISHED;
            }, platformTransactionManager)
            .build();
    }

    @Bean
    public Step chunkStep() {
        return new StepBuilder("chunkStep", jobRepository)
            .chunk(3, platformTransactionManager)
            .reader(() -> null)
            .processor(item -> "_" + item)
            .writer(items -> items.forEach(System.out::println))
            .build();
    }

    @Bean
    public Step step3() {
        return new StepBuilder("step1", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("dddddddd");
                return RepeatStatus.FINISHED;
            }, platformTransactionManager)
            .build();
    }

    @Bean
    public Step step4() {
        return new StepBuilder("step1", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("dddddddd");
                return RepeatStatus.FINISHED;
            }, platformTransactionManager)
            .build();
    }



}
