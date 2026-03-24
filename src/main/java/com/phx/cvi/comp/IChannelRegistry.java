package com.phx.cvi.comp;

import java.util.Set;

import io.netty.channel.Channel;

/**
 * Channel registry , store active session TCP channel
 */
public interface IChannelRegistry {
   public void add(Channel ch);
   public void remove(Channel ch);
   public Set<Channel> all();
   public Channel getChannel(String name);
}
