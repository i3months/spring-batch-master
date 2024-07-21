package io.springbatch.spring_batch_master.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class CustomItemReader implements ItemReader<String> {

    private List<String> list;

    public CustomItemReader(List<String> list) {
        this.list = new ArrayList<>(list);
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(!list.isEmpty()) return list.remove(0);

        return null;
    }
    
}
