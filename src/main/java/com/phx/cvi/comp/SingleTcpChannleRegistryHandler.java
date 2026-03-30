package com.phx.cvi.comp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Only allow one connection, if new connection created, the old connection
 * will isconnected
 */
@Sharable
public class SingleTcpChannleRegistryHandler extends ChannelInboundHandlerAdapter{
	private IChannelRegistry channelRegistry;
	public SingleTcpChannleRegistryHandler(IChannelRegistry iChannelRegistry) {
		this.channelRegistry = iChannelRegistry;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		Channel previousChannel = this.channelRegistry.getChannel("");
		if(previousChannel !=null ) {
			previousChannel.close();
		}

		this.channelRegistry.add(ctx.channel());

		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.channelRegistry.remove(ctx.channel());
		super.channelInactive(ctx);
	}
}
