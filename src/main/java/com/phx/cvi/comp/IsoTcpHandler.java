package com.phx.cvi.comp;

import com.cvj.msg.IsoMsg;
import com.phx.cvi.comp.mux.IsoMuxObject;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * handling incoming message
 */
@Sharable
public class IsoTcpHandler extends SimpleChannelInboundHandler<IsoMsg>{

	private ITcpInterface<IsoMsg> busInterface;
	private IMux<IsoMsg> mux;


	public IsoTcpHandler(ITcpInterface<IsoMsg> iBusInterface,IMux<IsoMsg> iMux) {
		super();
		this.busInterface = iBusInterface;
		this.mux = iMux;
	}

	/*on channel connected*/
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.busInterface.onActiveSession(ctx.channel());
	}

	/*on channel isconnected*/
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.busInterface.onInActiveSession(ctx.channel());
	}

	protected String constructChannelName(Channel ch) {
		//sementara
		return ch.toString();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IsoMsg msg) throws Exception {
		String channelName = constructChannelName(ctx.channel());
		if(msg.isRequest()) {
			this.busInterface.processRequest(channelName, msg);
		}else {
			IsoMuxObject<IsoMsg> muxObj = this.mux.getOriginalMsg(msg);
			if(muxObj == null) {
				//could be a late response
				this.busInterface.processResponse(null, channelName, null, msg);
			}else {
				//normal response
				this.busInterface.processResponse(muxObj.getSourceChannelName(), channelName, muxObj.getObj(), msg);
			}
		}
	}

}
