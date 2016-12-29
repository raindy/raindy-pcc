package com.archnotes.raindy.pcc.unit.app;

import com.archnotes.raindy.pcc.common.AppConfigBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;

/**
 * Created by zhangyouce on 2016/12/26.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
@PropertySource(value = {"application.properties"})
public class AppConfig extends AppConfigBase{


    @Value("#{'${rpc.zookeeper.nodes}'.split(',')}")
    public String[] rpcZookeeperNodes;
    @Value("${rpc.thrift.path}")
    public String rpcThriftPath;

    public @Bean String[] zkNodes() {
        return this.rpcZookeeperNodes;
    }
    public @Bean String thrfitPath() {
        return rpcThriftPath;
    }

    public @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
