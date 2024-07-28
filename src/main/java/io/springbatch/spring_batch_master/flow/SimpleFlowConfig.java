package io.springbatch.spring_batch_master.flow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import io.springbatch.spring_batch_master.config.CustomItemStreamReader;
import io.springbatch.spring_batch_master.config.CustomService;
import io.springbatch.spring_batch_master.config.Customer;
import io.springbatch.spring_batch_master.config.CustomerFieldSetMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleFlowConfig {


    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Job job(JobRepository jobRepository) {
        return new JobBuilder("helloJob", jobRepository)
            .start(step1(jobRepository, platformTransactionManager))
            .on("COMPLETED").to(step2(jobRepository, platformTransactionManager))
            .from(step1(jobRepository, platformTransactionManager))            
            .on("FAILED").to(flow())
            .end()
            .build();
    }

    @Bean
    public Job flowStepJob() {
        return new JobBuilder("flowJob", jobRepository)
        .start(flowStep())
        .next(step3(jobRepository, platformTransactionManager))
        .build();
    }

    @Bean
    @JobScope
    public Step flowStep() {
        return new StepBuilder("flowStep", jobRepository)
            .flow(flow())
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
    @JobScope
    public Step step1(@Value("#{jobParameters['message']}") String message, JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("STEP1", jobRepository)
            .<String, String>chunk(5, transactionManager)
            .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5")))
            .processor(new ItemProcessor<String, String>() {
                @Override
                public String process(String item) throws Exception {
                    Thread.sleep(300);
                    return "my" + item;
                }
            })
            .writer(new ItemWriter<String>() {
                @Override
                public void write(Chunk<? extends String> items) throws Exception {
                    System.out.println(items);
                }
                
            })
            .build();
    }


    public CustomItemStreamReader itemReader() {
        List<String> items = new ArrayList<>();

        for(int i=0; i<10; i++) {
            items.add("item" + String.valueOf(i));
            
        }
        return new CustomItemStreamReader(items);
    }

    @Bean
    @StepScope
    public Tasklet tasklet1(@Value("#{jobExecutionContext['name']}") String name) {
        System.out.println("name" + name);
        return (StepContribution, chunkContext) -> {
            System.out.println();
            return RepeatStatus.FINISHED;
        };
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

    @Bean
    public ItemReader<String> itemReaderAdapter() {
        ItemReaderAdapter<String> reader = new ItemReaderAdapter<>();
        reader.setTargetObject(customService());
        reader.setTargetMethod("customRead");
        return reader;
    }

    @Bean
    public Object customService() {
        return new CustomService<>();
    }

    @Bean
    public ItemReader itemReaderReturn() {
        return new FlatFileItemReaderBuilder<Customer>()
            .name("flatfile")
            .resource(new ClassPathResource("/customer.csv"))
            .fieldSetMapper(new CustomerFieldSetMapper())
            .linesToSkip(1)
            .delimited().delimiter(",")
            .names("name", "age", "year")
            .build();
    }

    @Bean
    public JdbcCursorItemReader<Object> itemReaderReturn1() {
        return new JdbcCursorItemReaderBuilder<>()
            .name("jdbcCursor")
            .fetchSize(10)
            .sql("SELECT * FROM CUSTOMER ORDER BY C_ID ASC")
            .beanRowMapper(null)
            .dataSource(null)
            .build();
    }

    @Bean
    public ItemReader<? extends String> customItemReader2() {
        return new JdbcPagingItemReaderBuilder()
            .name("jdbc")
            .pageSize(10)
            .dataSource(null)
            .rowMapper(null)
            .queryProvider(null)
            .build();
    }

    @Bean
    public PagingQueryProvider createQueryProvider() {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(null);
        queryProvider.setSelectClause(null);
        return null;
    }


}