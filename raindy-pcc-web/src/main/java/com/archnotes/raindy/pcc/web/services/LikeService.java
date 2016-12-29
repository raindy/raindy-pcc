package com.archnotes.raindy.pcc.web.services;

import com.archnotes.raindy.pcc.common.ClusterFactory;
import com.archnotes.raindy.pcc.unit.api.UnitService;
import com.twitter.finagle.Service;
import com.twitter.finagle.builder.ClientBuilder;
import com.twitter.finagle.stats.InMemoryStatsReceiver;
import com.twitter.finagle.thrift.ThriftClientFramedCodec;
import com.twitter.finagle.zookeeper.ZookeeperServerSetCluster;
import com.twitter.util.Duration;
import org.apache.thrift.protocol.TBinaryProtocol;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyouce on 2016/12/26.
 */
public class LikeService implements UnitService.Iface {

    private UnitService.FutureIface unitService;
    private Service service;
    public LikeService(String suffix, String[] nodes) {
        String packageName = UnitService.class.getPackage().getName();
        String serviceName = UnitService.class.getSimpleName();
        ClusterFactory clusterFactory = new ClusterFactory(suffix, nodes);
        ZookeeperServerSetCluster cluster = clusterFactory.getForService(serviceName);
        List onlineServers = clusterFactory.getOnlineServers(serviceName);
//        logger.warn("Online servers: " + onlineServers.toString());
        service = ClientBuilder.safeBuild(ClientBuilder.get().cluster(cluster).name(serviceName + " client")
                .failFast(false)
                .codec(ThriftClientFramedCodec.get())
                .timeout(Duration.apply(10L, TimeUnit.SECONDS))
                .retries(4)
                .hostConnectionLimit(1));
        UnitService.FinagledClient client = new UnitService.FinagledClient(service, new TBinaryProtocol.Factory(), serviceName, new InMemoryStatsReceiver());
        this.unitService = client;
    }

    public void close() {
        service.close();
    }

    @Override
    public Boolean addUser(String name) {
        return unitService.addUser(name).get();
    }

    @Override
    public String getUser(String userId) {
        return unitService.getUser(userId).get();
    }

    @Override
    public Boolean followUser(String fromId, String toId) {
        return unitService.followUser(fromId, toId).get();
    }

    @Override
    public Boolean like(String targetId, String ownId, String userId) {
        return unitService.like(targetId, ownId, userId).get();
    }

    @Override
    public Set<String> getLikes(String targetId) {
        return unitService.getLikes(targetId).get();
    }

    @Override
    public Long getLikesCount(String targetId) {
        return unitService.getLikesCount(targetId).get();
    }

    @Override
    public Boolean isLike(String targetId, String userId) {
        return unitService.isLike(targetId, userId).get();
    }
}
