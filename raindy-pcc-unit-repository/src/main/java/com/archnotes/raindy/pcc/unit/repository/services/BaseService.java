package com.archnotes.raindy.pcc.unit.repository.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyouce on 2016/12/27.
 */
public abstract class BaseService implements Runnable{

    protected Logger logger = Logger.getLogger(getClass());

    @Autowired
    protected JedisPool pool;
    @Autowired
    protected JdbcTemplate template;

//    private ServiceConfig serviceConfig;

    private int batchMax = 50;
    private String queueKey = "";
    private String valueSplit = ":";
    private String updateSql = "";
    private int intervalTime = 1024;

//    public BaseService(ServiceConfig serviceConfig) {
//        this.batchMax = serviceConfig.getBatchMax();
//        this.queueKey = serviceConfig.getQueueKey();
//        this.valueSplit = serviceConfig.getValueSplit();
//        this.updateSql = serviceConfig.getUpdateSql();
//    }

//    public void setIntervalTime(int intervalTime) {
//        this.intervalTime = intervalTime;
//    }

//    public int getIntervalTime() {
//        return this.intervalTime;
//    }

    public BaseService(int batchMax, String queueKey) {
        this(batchMax, queueKey, ":");
    }

    public BaseService(int batchMax, String queueKey, String valueSplit) {
        this.batchMax = batchMax;
        this.queueKey = queueKey;
        this.valueSplit = valueSplit;
    }


    private long getQueueSize() {
        try (Jedis jedis = pool.getResource()) {
            return jedis.llen(this.queueKey);
        }
    }

    public void optimize() {
        if (getQueueSize() > (batchMax * 2)) {
            if (intervalTime == 1) {
                return;
            }
            intervalTime = intervalTime >> 1;
        } else {
            if (intervalTime == 8192) {
                return;
            }
            intervalTime = intervalTime << 1;
        }
        logger.info("intervalTime:" + intervalTime);
    }

    @Override
    public void run() {

        while(true) {
            List<Object[]> values = new ArrayList<>();
            try (Jedis jedis = pool.getResource()) {
                int i = 0;
                String value = jedis.rpop(this.queueKey);
                while (i < this.batchMax) {
                    if (value == null) {
                        break;
                    }
                    logger.info(value);
                    values.add(value.split(this.valueSplit));
                    logger.info(values.size());
                    i++;
                    value = jedis.rpop(this.queueKey);
                }
            }
            if (values.size() > 0) {
                addBatch(values);
            }
            if (intervalTime > 0) {
                try {
                    Thread.sleep(intervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    protected void addBatch(List<Object[]> values) {
        this.template.batchUpdate(this.updateSql, values);
    }
}
