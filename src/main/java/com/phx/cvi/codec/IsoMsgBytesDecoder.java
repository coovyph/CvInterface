package com.phx.cvi.codec;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.cvj.encoder.EncDecException;
import com.cvj.encoder.IsoMsgEncoder;
import com.cvj.encoder.xml.IsoXmlMsgEncoder;
import com.cvj.msg.IsoMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Author      : Hendra (Coovy)
 * Date	       : 2026-04-04
 * Description : decode raw message bytes to IsoMsg object
 */
public class IsoMsgBytesDecoder extends SimpleChannelInboundHandler<ByteBuf>{
	private IsoMsgEncoder msgEncoder;

	public IsoMsgBytesDecoder(String xmlFileConfig)
			throws EncDecException,
			IOException,
			SAXException,
			ParserConfigurationException {
		this.msgEncoder = new IsoXmlMsgEncoder(xmlFileConfig);
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
		// TODO Auto-generated method stub
		IsoMsg msg = new IsoMsg();
		byte[] msgBytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(msgBytes);
		try {
			this.msgEncoder.decode(msgBytes, 0, msg);
			ctx.fireChannelRead(msg);
		}catch(EncDecException e) {
			ctx.fireExceptionCaught(e);
		}
	}

}
