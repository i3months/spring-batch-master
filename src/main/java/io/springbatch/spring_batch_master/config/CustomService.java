package io.springbatch.spring_batch_master.config;

public class CustomService<T> {

    private int cnt = 0;

    public T customRead() {
        return (T)("item" + cnt);
    }
    
}
