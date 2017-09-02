package com.pzybrick.iote2e.stream.router;

import java.io.Serializable;
import java.nio.ByteBuffer;

public interface RouterBdbb extends AutoCloseable, Serializable{
    public void add(ByteBuffer byteBuffer) throws Exception;
    public void flush() throws Exception;
    public int getBatchSize();
    public void setBatchSize(int batchSize);
}
