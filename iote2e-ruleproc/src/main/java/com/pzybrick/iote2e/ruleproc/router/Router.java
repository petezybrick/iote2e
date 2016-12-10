package com.pzybrick.iote2e.ruleproc.router;

import java.io.Serializable;

import com.pzybrick.iote2e.schema.avro.Iote2eRequest;

public interface Router extends AutoCloseable, Serializable{
    public void add(Iote2eRequest iote2eRequest) throws Exception;
    public void flush() throws Exception;
    public int getBatchSize();
    public void setBatchSize(int batchSize);
}
