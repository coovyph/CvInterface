package com.phx.cvi.codec;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Hex;
import org.xml.sax.SAXException;

import com.cvj.encoder.EncDecException;
import com.cvj.encoder.IsoMsgEncoder;
import com.cvj.encoder.xml.IsoXmlMsgEncoder;
import com.cvj.msg.IsoMsg;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Author      : Hendra (Coovy)
 * Date	       : 2026-04-04
 * Description : Encode message from object IsoMsg to bytes
 */
@Slf4j
public class IsoMsgBytesEncoder extends MessageToMessageEncoder<IsoMsg>{

	private IsoMsgEncoder msgEncoder;

	public IsoMsgBytesEncoder(String xmlFileConfig)
			throws EncDecException,
			IOException,
			SAXException,
			ParserConfigurationException {
		this.msgEncoder = new IsoXmlMsgEncoder(xmlFileConfig);
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, IsoMsg msg, List<Object> out) throws Exception {

		try {
			byte[] msgBytes = this.msgEncoder.encode(msg);
			log.info("Outgoing :\n{}",msg.writeDebugString("  "));
			log.info("Using message bytes {} : " + Hex.encodeHexString(msgBytes));
			out.add(Unpooled.wrappedBuffer(msgBytes)); // LengthFieldPrepender will add header
		} catch (EncDecException e) {
			ctx.fireExceptionCaught(e);
		}
	}

}
