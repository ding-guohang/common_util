package com.ca.log.monitor;

/**
 * Monitor interface
 *
 * @author guohang.ding on 16-11-22
 */
public interface Monitor {

    void recordOne(String name, long time);

    void recordOne(String name);

    void decrRecord(String name);

    void recordMany(String name, long count, long time);

    void recordSize(String name, long size);

    void recordValue(String name, long count);

}
