package com.phx.cvi.comp;

import java.util.Set;

import io.netty.channel.Channel;

/**
 * Channel registry , store active session TCP channel
 */
public interface IChannelRegistry {
	public String constructChannelName(Channel c);
	public boolean add(Channel ch);
	public boolean remove(Channel ch);
	public Set<String> keySet();
	public Channel getChannel(String name);
}
