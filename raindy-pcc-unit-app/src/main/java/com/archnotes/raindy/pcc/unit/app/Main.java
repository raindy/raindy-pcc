package com.archnotes.raindy.pcc.unit.app;

import com.archnotes.raindy.pcc.common.ClusterFactory;
import com.archnotes.raindy.pcc.unit.api.UnitService;
import com.archnotes.raindy.pcc.unit.app.Handler.UnitHandler;
import com.twitter.finagle.builder.Server;
import com.twitter.finagle.builder.ServerBuilder;
import com.twitter.finagle.thrift.ThriftServerFramedCodec;
import com.twitter.ostrich.admin.AdminHttpService;
import com.twitter.ostrich.admin.RuntimeEnvironment;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.net.InetSocketAddress;
import java.util.Properties;

public class Main {

	public static void main(String[] args) {



		String ip = "";
		int port = 0;
		String node = "";
		if (args.length > 0) {
			ip = args[0];
			port = Integer.parseInt(args[1]);
			node = args[2];

			Properties pp = System.getProperties();
			pp.put("node", node);
		}

		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

		UnitHandler handler = ctx.getBean(UnitHandler.class);
		if (ip.equals("")) {
//			ip = (String) ctx.getBean("serverIp");
			ip = "localhost";
		}
		if (port == 0) {
//			port = (Integer) ctx.getBean("serverPort");
			port = 10002;
		}
		System.out.println("Main port: "+port);

		String[] zkNodes = (String[]) ctx.getBean("zkNodes");
		String thriftPath = (String) ctx.getBean("thrfitPath");
		
		String packageName = UnitService.class.getPackage().getName();
		String ServiceName = UnitService.class.getSimpleName();

		System.out.println("zkNodes: " + zkNodes[0]);
		System.out.println("thriftPath: " + thriftPath);

		Server server = ServerBuilder.safeBuild(new UnitService.FinagledService(handler,
				new TBinaryProtocol.Factory()), 
				ServerBuilder.get()
				.name(ServiceName)
				.codec(ThriftServerFramedCodec.get())
				.maxConcurrentRequests(50)
				.bindTo(new InetSocketAddress(ip, port)));
		
		ClusterFactory cluster = new ClusterFactory(thriftPath + node + "/", zkNodes);
		cluster.reportServerUpAndRunning(server, ServiceName);

		System.out.println("The server, running from port " + port + " joined the " + ServiceName + " cluster.");

		int ostrichPort = port + 1;
		
		RuntimeEnvironment runtime = new RuntimeEnvironment("");
		AdminHttpService admin = new AdminHttpService(ostrichPort, 0, runtime);
		admin.start();
		System.out.println("Ostrich reporting started on port " + ostrichPort);

//		ctx.close();
	}
}
