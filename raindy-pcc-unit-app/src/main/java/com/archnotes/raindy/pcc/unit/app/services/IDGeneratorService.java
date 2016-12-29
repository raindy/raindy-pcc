package com.archnotes.raindy.pcc.unit.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyouce on 2016/12/24.
 */
@Service
public class IDGeneratorService {

    private int id;

    @Autowired
    public IDGeneratorService(JdbcTemplate jdbcTemplate) {
        id = jdbcTemplate.queryForObject("select case when max(user_id) is null then 0 else max(user_id) end from account", Integer.class);
    }

    public synchronized int getNext() {
        return ++id;
    }
}
