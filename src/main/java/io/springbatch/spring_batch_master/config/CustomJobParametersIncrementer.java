package io.springbatch.spring_batch_master.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class CustomJobParametersIncrementer implements JobParametersIncrementer {

    static final DateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");
    @Override
    public JobParameters getNext(JobParameters parameters) {
        String id = format.format(new Date());
        
        return new JobParametersBuilder().addString("run.id", id).toJobParameters();
    }
    
}
