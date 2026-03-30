package com.phx.cvi.comp.chreg;

import java.util.Set;

import com.phx.cvi.comp.IChannelRegistry;

import io.netty.channel.Channel;

/*
 * Single client only with single name
 */
public class SingleChannelRegistry implements IChannelRegistry{
	private String channelName;
	private Channel channel;

	public SingleChannelRegistry(String iChannelName) {
		this.channelName = iChannelName;
	}

	@Override
	public String constructChannelName(Channel c) {
		return this.channelName;
	}

	@Override
	public void add(Channel ch) {
		this.channel = ch;
	}

	@Override
	public void remove(Channel ch) {
		//do nthing
		this.channel = null;
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Channel getChannel(String name) {
		return this.channel;
	}
}
