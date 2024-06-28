package io.springbatch.spring_batch_master.config;

import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.stereotype.Component;

@Component
public class JobParameterTest {
    JobParameters jobParameters = new JobParametersBuilder()
        .addString("name", "user")
        .addLong("seq", 2L)
        .addDate("date", new Date())
        .toJobParameters();
}
