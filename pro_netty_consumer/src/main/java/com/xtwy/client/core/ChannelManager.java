package com.xtwy.client.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.channel.ChannelFuture;

public class ChannelManager {
	
	static AtomicInteger postion= new AtomicInteger();
    //static Set<String> realserverPath = new HashSet<String>();
	static CopyOnWriteArrayList<String> realserverPath = new CopyOnWriteArrayList<String>();
	public static CopyOnWriteArrayList<ChannelFuture> channelFutures = new CopyOnWriteArrayList<>();
	
	
	public static void removeChannel(ChannelFuture channel) {
		channelFutures.remove(channel);
	}
	
    public static void add(ChannelFuture channel) {
    	channelFutures.add(channel);
    }
    
    public static void clear() {
    	channelFutures.clear();
    }

	public static ChannelFuture get(AtomicInteger i) {
		int  size = channelFutures.size();
		ChannelFuture channel = null;
        if(i.incrementAndGet()>size) {
        	channel = channelFutures.get(0);
            ChannelManager.postion = new AtomicInteger(1);
        }else {
        	channel = channelFutures.get(i.getAndIncrement());
		}
        
        
        if(!channel.channel().isActive()) {
        	channelFutures.remove(channel);
        	return get(postion);
        }
        	
		return channel;
	}
	
}
