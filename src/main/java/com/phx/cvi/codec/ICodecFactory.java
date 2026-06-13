package com.phx.cvi.codec;

import java.util.Map;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public interface ICodecFactory {
	public ChannelInboundHandlerAdapter createDecoder();
	public ChannelOutboundHandlerAdapter createEncoder();
	public void setProperties(Map<String,String> inProperties);
}
