package com.archnotes.raindy.pcc.web;

import com.archnotes.raindy.pcc.common.AppConfigBase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by zhangyouce on 2016/12/24.
 */
@Configuration
@ComponentScan(basePackages = "com.archnotes.raindy.pcc.web")
public class AppConfig extends AppConfigBase {
}
