package com.phx.cvi.comp.chreg;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.phx.cvi.comp.IChannelRegistry;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TcpChannelRegistry implements IChannelRegistry{
	private Map<String,Channel> channels;
	private List<String> keys;
	private int maxSessions = Integer.MAX_VALUE;
	private boolean removeOldChannel = false;
	public TcpChannelRegistry(int inMaxSessions,boolean inRemoveOldChannel) {
		this.channels = new HashMap<>();
		this.keys = new ArrayList<>();
		this.maxSessions = inMaxSessions;
		this.removeOldChannel = inRemoveOldChannel;
	}

	@Override
	public boolean add(Channel ch) {
		String name = constructChannelName(ch);
		if(channels.size() >= this.maxSessions) {
			if(this.removeOldChannel) {
				String firstChannelName = keys.remove(0);
				Channel oldChannel = this.channels.remove(firstChannelName);
				oldChannel.close();
				log.info("Max session {} is exceeded, remove old connection",this.maxSessions);
			}else {
				ch.close();
				log.info("Max session {} is exceeded, close new connection",this.maxSessions);
				return false;
			}
		}
		this.keys.add(name);
		this.channels.put(name, ch);
		log.info("New channel {} is registered",name);
		return true;
	}

	@Override
	public boolean remove(Channel ch) {
		String name = constructChannelName(ch);
		Channel oldChannel = this.channels.remove(name);
		if(oldChannel == null) {
			return !this.removeOldChannel;
		}else {
			log.info("Channel {} removed",name);
			return this.removeOldChannel;
		}
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return this.channels.keySet();
	}

	@Override
	public Channel getChannel(String name) {
		return this.channels.get(name);
	}

	@Override
	public String constructChannelName(Channel c) {
		InetSocketAddress socketAddress =	(InetSocketAddress)c.remoteAddress();
		StringBuilder sb = new StringBuilder();
		sb.append(socketAddress.getHostName());
		sb.append(".");
		sb.append(socketAddress.getPort());
		return sb.toString();
	}

}
