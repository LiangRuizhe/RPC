
package LiangRuizhe.rpc.cousumer.core;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;

import com.alibaba.fastjson.JSONObject;

import LiangRuizhe.rpc.cousumer.constans.Constans;
import LiangRuizhe.rpc.cousumer.handler.SimpleClientHandler;
import LiangRuizhe.rpc.cousumer.param.ClientRequest;
import LiangRuizhe.rpc.cousumer.param.Response;
import LiangRuizhe.rpc.cousumer.zk.ServerWatcher;
import LiangRuizhe.rpc.cousumer.zk.ZooKeeperFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClient {
//	public static Set<String> realServerPath = new HashSet<String>();//去重and去序列号
	public static final Bootstrap b = new Bootstrap();
//定义了一个静态的Bootstrap实例b,Bootstrap是Netty用于配置客户端的类，用于设置Netty的各种配置并启动客户端。
	private static ChannelFuture f = null;
//定义了一个静态变量f，表示客户端连接的ChannelFuture对象，用来跟踪和处理链接的生命周期。
	static{
		String host = "localhost";
		int port = 8080;
		
		EventLoopGroup work = new NioEventLoopGroup();//EventLoopGroup，管理Netty事件循环线程池，基于Java NIO提供非阻塞的IO操作。
		try {
		b.group(work) //将work设置为处理I/O事件的线程池
			.channel(NioSocketChannel.class) //使用NioSocketChannel作为客户端的连接类型，是Netty用于TCP/IP连接的类
			.option(ChannelOption.SO_KEEPALIVE, true) 
			.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							
							ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
							ch.pipeline().addLast(new StringDecoder());//字符串解码器
							ch.pipeline().addLast(new StringEncoder());//字符串编码器
							ch.pipeline().addLast(new SimpleClientHandler());//业务逻辑处理处
						}
			});
				
				CuratorFramework client = ZooKeeperFactory.getClient();
			//通过ZooKeeperFactory.getClient()方法获取一个ZooKeeper客户端实例。这个客户端用于与ZooKeeper进行通信，以获取服务端的地址和监听服务端的变化
				List<String> serverPath = client.getChildren().forPath(Constans.SERVER_PATH);
				//客户端加上ZK监听服务器的变化,通过ZooKeeper客户端获取SERVER_PATH路径下的所有子节点（即所有注册的服务器地址）
				CuratorWatcher watcher = new ServerWatcher();
				client.getChildren().usingWatcher(watcher ).forPath(Constans.SERVER_PATH);
				//为ZooKeeper注册一个Watcher，监听该路径下的子节点变化
				for(String path :serverPath){
					String[] str = path.split("#");
					ChannelManager.realServerPath.add(str[0]+"#"+str[1]);
					ChannelFuture channnelFuture = NettyClient.b.connect(str[0], Integer.valueOf(str[1]));
					ChannelManager.addChnannel(channnelFuture);
				}
				if(ChannelManager.realServerPath.size()>0){
					String[] netMessageArray = ChannelManager.realServerPath.toArray()[0].toString().split("#");
					host = netMessageArray[0];
					port = Integer.valueOf(netMessageArray[1]);
				}
			
//			f = b.connect(host, port).sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Response send(ClientRequest request){
		f=ChannelManager.get(ChannelManager.position);
		f.channel().writeAndFlush(JSONObject.toJSONString(request)+"\r\n");
//		f.channel().writeAndFlush("\r\n");
		Long timeOut = 60l;
		ResultFuture future = new ResultFuture(request);
//		return future.get(timeOut);
		return future.get(timeOut);

	}
	
}
