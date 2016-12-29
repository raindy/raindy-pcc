package com.archnotes.raindy.pcc.unit.repository.services;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangyouce on 2016/12/24.
 */
@Service
public class LikeService extends BaseService{

    public LikeService() {
        this(100, "like");
    }

    public LikeService(int batchMax, String queueKey) {
        super(batchMax, queueKey);
    }

    protected void addBatch(List<Object[]> values) {
        this.template.batchUpdate("insert into target_like(target_id, own_id, user_id, `type`) values(?,?,?,?)", values);
    }


}
