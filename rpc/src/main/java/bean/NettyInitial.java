package bean;

import java.net.InetAddress;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import constants.Constans;
import factory.ZooKeeperFactory;
import handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

@Component
public class NettyInitial implements ApplicationListener<ContextRefreshedEvent> {
	//NettyInitial 被标注为 @Component，它是一个 Spring 管理的 Bean。并且 NettyInitial 实现了 ApplicationListener<ContextRefreshedEvent> 接口
	//因此在 Spring 容器刷新完成后，ContextRefreshedEvent 事件会触发 onApplicationEvent 方法
	public  void start() {		
		NioEventLoopGroup boss = new NioEventLoopGroup();
		NioEventLoopGroup work = new NioEventLoopGroup();
			
		try {//启动辅助
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(boss, work)
				   .option(ChannelOption.SO_BACKLOG, 128)//设置TCP队列大小:包含已连接+未连接
				   .option(ChannelOption.SO_KEEPALIVE, false)//不使用默认的心跳机制
				   .channel(NioServerSocketChannel.class)
				   .childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// 设置\r\n为分隔符
						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()[0]));
						ch.pipeline().addLast(new StringDecoder());//字符串解码器
//						ch.pipeline().addLast(new IdleStateHandler(20, 15, 10, TimeUnit.SECONDS));
						ch.pipeline().addLast(new ServerHandler());//业务逻辑处理处
						ch.pipeline().addLast(new StringEncoder());//字符串编码器
					}
				   });
	//Netty服务器启动
			int port = 8080;
			ChannelFuture f = serverBootstrap.bind(8080).sync();
		
			InetAddress address = InetAddress.getLocalHost();
			CuratorFramework client = ZooKeeperFactory.getClient();
			if(client != null){
				System.out.println(client);
				client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(Constans.SERVER_PATH+"/"+address.getHostAddress()+"#"+port+"#");
				System.out.println("成功");
          //与 ZooKeeper 的交互，将服务器的地址（IP 和端口）注册到 ZooKeeper 上
			}
		
			f.channel().closeFuture().sync();
		
			System.out.println("Closed");
		} catch (Exception e) {
			e.printStackTrace();
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}
	
	}

	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		this.start();
		//触发onApplicationEvent,调用start方法。
	}
	
	
	
}
