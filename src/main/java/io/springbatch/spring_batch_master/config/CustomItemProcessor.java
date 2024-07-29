package io.springbatch.spring_batch_master.config;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<String, String>{

    int cnt = 0;

    @Override
    public String process(String item) throws Exception {
        cnt++;
        return (item + cnt).toUpperCase();
    }
    
}
