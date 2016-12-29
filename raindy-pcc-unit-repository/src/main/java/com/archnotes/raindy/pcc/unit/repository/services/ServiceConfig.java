package com.archnotes.raindy.pcc.unit.repository.services;

/**
 * Created by zhangyouce on 2016/12/27.
 */
public class ServiceConfig {
    private int batchMax = 50;
    private String queueKey = "";
    private String valueSplit = ":";
    private String updateSql = "";


    public int getBatchMax() {
        return batchMax;
    }

    public void setBatchMax(int batchMax) {
        this.batchMax = batchMax;
    }

    public String getQueueKey() {
        return queueKey;
    }

    public void setQueueKey(String queueKey) {
        this.queueKey = queueKey;
    }

    public String getValueSplit() {
        return valueSplit;
    }

    public void setValueSplit(String valueSplit) {
        this.valueSplit = valueSplit;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }
}
