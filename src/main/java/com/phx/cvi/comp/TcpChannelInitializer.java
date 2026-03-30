package com.phx.cvi.comp;

import java.util.List;

import com.cvj.msg.IMsg;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class TcpChannelInitializer<T extends IMsg> extends ChannelInitializer<SocketChannel>{

	private List<ChannelInboundHandler> channelInboundHandlers;
	private List<ChannelOutboundHandler> channelOutboundHandlers;
	private SimpleChannelInboundHandler<T> tcpHandler;
	public TcpChannelInitializer(List<ChannelInboundHandler> iChannelInboundHandlers,
			List<ChannelOutboundHandler> iChannelOutboundHanlders,
			SimpleChannelInboundHandler<T> iTcpHandler) {
		this.channelInboundHandlers = iChannelInboundHandlers;
		this.channelOutboundHandlers = iChannelOutboundHanlders;
		this.tcpHandler = iTcpHandler;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		final ChannelPipeline cp = ch.pipeline();
		//inbound
		this.channelInboundHandlers.forEach(h->{
			cp.addLast(h);
		});
		//processor (business logic)
		cp.addLast(this.tcpHandler);

		//outbound
		this.channelOutboundHandlers.forEach(h->{
			cp.addLast(h);
		});


	}

}
