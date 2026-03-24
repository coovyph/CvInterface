package com.phx.cvi.comp;

import io.netty.channel.Channel;

public class TcpSender<T> implements ISender<T>{
	private IChannelRegistry channelRegistry;

	public TcpSender(IChannelRegistry iChannelRegisry) {
		this.channelRegistry = iChannelRegisry;
	}

	@Override
	public int send(String channelName, T msg) {
		Channel ch = channelRegistry.getChannel(channelName);
		ch.writeAndFlush(msg);
		return 0;
	}

	@Override
	public int send(T msg) {
		return send(null,msg);
	}

}
