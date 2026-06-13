package com.phx.cvi.codec;

import java.util.Map;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * Author      : Hendra (Coovy)
 * Date	       : 2026-04-04
 * Description : encoder and decoder creator for ISO8583 message
 */
@Slf4j
public class IsoMsgCodecFactory implements ICodecFactory{

	private String xmlFileName;
	@Override
	public ChannelInboundHandlerAdapter createDecoder() {
		try {
			return new IsoMsgBytesDecoder(this.xmlFileName);
		}catch(Exception e) {
			log.error("Cannot create msg bytes decoder using config " + this.xmlFileName,e);
			return null;
		}
	}

	@Override
	public ChannelOutboundHandlerAdapter createEncoder() {
		try {
			return new IsoMsgBytesEncoder(this.xmlFileName);
		}catch(Exception e) {
			log.error("Cannot create msg bytes encoder using config " + this.xmlFileName,e);
			return null;
		}
	}

	@Override
	public void setProperties(Map<String, String> inProperties) {
		this.xmlFileName = inProperties.get("config");
	}

}
