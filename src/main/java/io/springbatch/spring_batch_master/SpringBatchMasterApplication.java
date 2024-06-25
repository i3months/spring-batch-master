package io.springbatch.spring_batch_master;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
public class SpringBatchMasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchMasterApplication.class, args);
	}

}
