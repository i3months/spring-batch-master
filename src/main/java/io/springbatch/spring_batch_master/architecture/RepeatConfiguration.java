package io.springbatch.spring_batch_master.architecture;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

public class RepeatConfiguration {
    
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("step1", jobRepository)
            .<String, String>chunk(5, transactionManager)
            .reader(new ItemReader<>() {
                int i=0;
                @Override 
                public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                    i++;
                    return i>3 ? null : "itm" + i;
                }
            })
            .processor(new ItemProcessor<String ,String>() {                                
                
                @Override
                public String process(String item) throws Exception {

                    RepeatTemplate repeatTemplate = new RepeatTemplate();
                
                    repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(3));
                    repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(3000));

                    CompositeCompletionPolicy completionPolicy = new CompositeCompletionPolicy();
                    CompletionPolicy[] completionPolicies = new CompletionPolicy[]{
                        new SimpleCompletionPolicy(3),
                        new TimeoutTerminationPolicy(3000)
                    };
                    
                    completionPolicy.setPolicies(completionPolicies);
                    repeatTemplate.setCompletionPolicy(completionPolicy);

                    repeatTemplate.setExceptionHandler(new SimpleLimitExceptionHandler(3));
                    

                    repeatTemplate.iterate(new RepeatCallback() {
                        @Override
                        public RepeatStatus doInIteration(RepeatContext context) throws Exception {
                            System.out.println("testing...");
                            return RepeatStatus.CONTINUABLE;
                        }
                        
                    });

                    return item;
                }
            })
            .writer(items -> System.out.println(items))
            .build();
            
    }
}
