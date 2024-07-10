package io.springbatch.spring_batch_master.config;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class CustomDecider implements JobExecutionDecider {
    private int cnt = 0;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        cnt++;

        if (cnt % 2 ==0) {
            return new FlowExecutionStatus("FIRST");
        } 
        else {
            return new FlowExecutionStatus("SECOND");
        }
    }
}
        