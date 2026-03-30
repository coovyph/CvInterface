package com.phx.cvi.comp.chreg;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.phx.cvi.comp.IChannelRegistry;

import io.netty.channel.Channel;

public class TcpChannelRegistry implements IChannelRegistry{
	private Map<String,Channel> channels;
	public TcpChannelRegistry() {
		this.channels = new HashMap<>();
	}

	@Override
	public void add(Channel ch) {
		String name = constructChannelName(ch);
		this.channels.put(name, ch);
	}

	@Override
	public void remove(Channel ch) {
		String name = constructChannelName(ch);
		this.channels.remove(name);
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
