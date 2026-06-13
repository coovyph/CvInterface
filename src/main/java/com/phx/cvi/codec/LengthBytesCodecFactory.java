package com.phx.cvi.codec;

import java.util.Map;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class LengthBytesCodecFactory implements ICodecFactory{
	public static final String F_MAX_FRAME_LENGTH = "maxFrameLength";
	public static final String F_LENGTH_FIELD_OFFSET = "lengthFieldOffset";
	public static final String F_LENGTH_FIELD_LENGTH = "lengthFieldLength";
	public static final String F_INITIAL_BYTES_TO_STRIP = "initialBytesToStrip"; 
	public static final String F_LENGTH_FIELD_INCLUDE = "lengthFieldInclude";

	private int maxFrameLength;
	private int lengthFieldOffset;
	private int lengthFieldLength;
	private int initialBytesToStrip;
	private boolean lengthFieldInclude;

	@Override
	public ChannelInboundHandlerAdapter createDecoder() {		

		return new LengthFieldBasedFrameDecoder(
				maxFrameLength,
				lengthFieldOffset,
				lengthFieldLength,
				0, initialBytesToStrip);
	}

	private int getIntProp(Map<String,String> properties, String name,int defaultValue) {
		String val = properties.get(name);
		if(val == null)return defaultValue;
		try {
			return Integer.parseInt(val);
		}catch(NumberFormatException e) {
			return defaultValue;
		}
	}

	private boolean getBoolProp(Map<String,String> inProperties,String name) {
		String val = inProperties.get(name);
		return "true".equalsIgnoreCase(val);
	}

	@Override
	public ChannelOutboundHandlerAdapter createEncoder() {
		return new LengthFieldPrepender(this.lengthFieldLength, this.lengthFieldInclude);
	}

	@Override
	public void setProperties(Map<String, String> inProperties) {
		this.maxFrameLength = getIntProp(inProperties,F_MAX_FRAME_LENGTH, 1024);
		this.lengthFieldOffset = getIntProp(inProperties,F_LENGTH_FIELD_OFFSET, 0);
		this.lengthFieldLength = getIntProp(inProperties,F_LENGTH_FIELD_LENGTH, 2);
		this.initialBytesToStrip = getIntProp(inProperties,F_INITIAL_BYTES_TO_STRIP, 2);
		this.lengthFieldInclude = getBoolProp(inProperties,F_LENGTH_FIELD_INCLUDE);
	}



}
