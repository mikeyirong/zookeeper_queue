package eccang.zookeeper_server;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import eccang.zookeeper_server.entity.UrlMessage;

public class AppServer {
 public static void main(String[] args) {
	ZkClient zkClient = new ZkClient("localhost:2181,localhost:2182,localhost:2183",5000,5000,new SerializableSerializer());
	String root = "/book";
	DistributeSimpleQueue<UrlMessage> queue = new DistributeSimpleQueue<UrlMessage>(zkClient,root);
	long startTime = System.currentTimeMillis();
	for(int i=0;i<5000;i++) {
		UrlMessage urlMq1 = new UrlMessage();
		urlMq1.setNumber(i);
		urlMq1.setStatu(0);
		urlMq1.setUrl("http://www.amazon.com");
		urlMq1.setType(1);
		queue.offer(urlMq1);
	}
	long stopTime = System.currentTimeMillis();
	System.out.println("spend time is:"+(stopTime-startTime));
	zkClient.close();
}
}
