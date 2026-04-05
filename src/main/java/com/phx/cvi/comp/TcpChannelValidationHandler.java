package com.phx.cvi.comp;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

//disconnect the connection if , connection come from invalid remote address
@Slf4j
@Sharable
public class TcpChannelValidationHandler extends ChannelInboundHandlerAdapter{
	private Map<String,Integer> ipMap;
	private IChannelRegistry channelRegistry;

	public TcpChannelValidationHandler(IChannelRegistry inChannelRegistry) {
		this.ipMap = new HashMap<>();
		this.channelRegistry = inChannelRegistry;
	}

	public void append(String ip, int port) {
		this.ipMap.put(ip, port);
	}


	private boolean validateRemoteAddress(Channel c) {
		if(this.ipMap.isEmpty())return true;

		InetSocketAddress socketAddr = (InetSocketAddress)c.remoteAddress();
		String ip = socketAddr.getHostName();

		Integer port = this.ipMap.get(ip);
		if(port == null) {
			log.info("Remote ip {} does not allowed",ip);
			return false;
		}
		if(port != 0) {
			if(port!=socketAddr.getPort()) {
				log.info("Remote ip {} and port {} does not allowed",ip);
				return false;
			}
		}
		return true;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		if(!this.channelRegistry.remove(ctx.channel()))
			super.channelInactive(ctx);		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Channel c = ctx.channel();
		if(!validateRemoteAddress(c)) {
			ctx.close();
			return;
		}
		if(this.channelRegistry.add(c))
			super.channelActive(ctx);
	}

}
