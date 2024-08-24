package io.springbatch.spring_batch_master.multithread;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class NoSynchronizedConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private int count = 0;

    @Bean
    public Job job() {
        return new JobBuilder("job", jobRepository)
            .start(step1())
            .on("COMPLETED").to(step3())
            .from(step1())
            .on("FAILED").to(step2())
            .end()
            .build();
    }
}
