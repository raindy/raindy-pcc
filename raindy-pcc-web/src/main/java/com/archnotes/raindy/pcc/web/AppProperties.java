package com.archnotes.raindy.pcc.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyouce on 2016/12/26.
 */
@Component
public class AppProperties {
//    @Value("${rpc.zookeeper.nodes}")
    @Value("#{'${rpc.zookeeper.nodes}'.split(',')}")
    public String[] rpcZookeeperNodes;
    @Value("${rpc.thrift.path}")
    public String rpcThriftPath;
    public AppProperties() {
        System.out.println("app properties");
    }
}
