package com.archnotes.raindy.pcc.unit.repository.services;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyouce on 2016/12/24.
 */
@Service
public class FollowService extends BaseService{

    public FollowService() {
        this(50, "follow_new");
    }

    public FollowService(int batchMax, String queueKey) {
        super(batchMax, queueKey);
    }

    protected void addBatch(List<Object[]> values) {
        this.template.batchUpdate("insert into follow(source_uid, follow_uid) values(?, ?)", values);
    }


}
