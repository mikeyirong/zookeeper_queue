package eccang.zookeeper_server;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import eccang.zookeeper_server.entity.UrlMessage;

public class AppClient {
  public static void main(String[] args) {
	  ZkClient zkClient = new ZkClient("localhost:2183,localhost:2182,localhost:2183",5000,5000,new SerializableSerializer());
		String root = "/book";
		DistributeSimpleQueue<UrlMessage> queue = new DistributeSimpleQueue<UrlMessage>(zkClient,root);
		long startTime  = System.currentTimeMillis();
		while(!queue.isEmpty()) {
			UrlMessage msg = queue.poll();
			System.out.println(msg.toString());
		}	
		long stopTime = System.currentTimeMillis();
		
		System.out.println("spend time is:"+(stopTime-startTime));
}
}
