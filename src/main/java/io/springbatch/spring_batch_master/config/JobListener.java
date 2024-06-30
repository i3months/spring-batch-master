package io.springbatch.spring_batch_master.config;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class JobListener implements JobExecutionListener {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public void afterJob(JobExecution jobExecution) {
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        
        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, new JobParametersBuilder().addString("requestDate", "20210102").toJobParameters());

        if (lastJobExecution != null) {
            for(StepExecution execution : lastJobExecution.getStepExecutions()) {
                BatchStatus status = execution.getStatus();
                log.info(status);
                //..
            }
        }
    }
}
