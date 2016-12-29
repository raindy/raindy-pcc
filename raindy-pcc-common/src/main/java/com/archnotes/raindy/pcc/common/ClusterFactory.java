package com.archnotes.raindy.pcc.common;

import com.google.common.collect.ImmutableSet;
import com.twitter.common.net.pool.DynamicHostSet;
import com.twitter.common.quantity.Amount;
import com.twitter.common.quantity.Time;
import com.twitter.common.zookeeper.ServerSet;
import com.twitter.common.zookeeper.ServerSetImpl;
import com.twitter.common.zookeeper.ZooKeeperClient;
import com.twitter.finagle.builder.Server;
import com.twitter.finagle.zookeeper.ZookeeperServerSetCluster;
import com.twitter.thrift.ServiceInstance;
import scala.collection.JavaConversions;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ClusterFactory {
	public ClusterFactory(String basePath, String[] zkNodes) {
		this(basePath, zkNodes, 15);
	}
	
	public ClusterFactory(String basePath, String[] zkNodes, int timeout) {
		this.sessionTimeout = Amount.of(timeout, Time.SECONDS);
		this.basePath = basePath;
		this.nodes = new ArrayList<InetSocketAddress>();
		for (String str : zkNodes) {
			String[] arrs = str.split(":");
			this.nodes.add(new InetSocketAddress(arrs[0], Integer.parseInt(arrs[1])));
		}
		
	}
	
    private Amount<Integer,Time> sessionTimeout = null;
    private List<InetSocketAddress> nodes = null;
    private String basePath = "/finagle/test/";

    public ZookeeperServerSetCluster getForService(String clusterName) {
    	
        ServerSet serverSet = new ServerSetImpl(getZooKeeperClient(), getPath(clusterName));
        return new ZookeeperServerSetCluster(serverSet);
    }

    public void reportServerUpAndRunning(Server server, String clusterName) {
        getForService(clusterName).join(server.localAddress(), 
        		new scala.collection.immutable.HashMap());
    }
    
    
    public void reportServerUpAndRunning(SocketAddress socketAddress, String clusterName) {
        getForService(clusterName).join(socketAddress, 
        		new scala.collection.immutable.HashMap());
    }
    
    public void reportServerUpAndRunning(String ip, int port, String clusterName) {
        getForService(clusterName).join(new InetSocketAddress(ip, port), 
        		new scala.collection.immutable.HashMap());
    }

    public List<SocketAddress> getOnlineServers(String clusterName) {
        try {
            ZookeeperServerSetCluster cluster = getForService(clusterName);
            // Run the monitor() method, which will block the thread until the initial list of servers arrives.
            new ServerSetImpl(zooKeeperClient, getPath(clusterName)).monitor(new DynamicHostSet.HostChangeMonitor<ServiceInstance>(){
                public void onChange(ImmutableSet<ServiceInstance> serviceInstances) {
                    // do nothing
                }
            });
            return JavaConversions.asJavaList(cluster.snap()._1());
        } catch (DynamicHostSet.MonitorException e) {
            throw new RuntimeException("Couldn't get list of online servers", e);
        }
    }

    private String getPath(String clusterName) {
        return this.basePath + clusterName;
    }

    private ZooKeeperClient zooKeeperClient;
    private ZooKeeperClient getZooKeeperClient() {
        if (zooKeeperClient == null) {
            zooKeeperClient = new ZooKeeperClient(sessionTimeout, nodes);
        }
        return zooKeeperClient;
    }
}
