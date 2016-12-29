package com.archnotes.raindy.pcc.unit.app;

import com.archnotes.raindy.pcc.unit.app.services.FollowService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
/**
 * Created by zhangyouce on 2016/12/27.
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestConfig.class, AppConfig.class})
public class FollowServiceTest {

    @Autowired
    private FollowService followService;

}
