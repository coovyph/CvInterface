package com.phx.cvi.comp;

import com.cvj.msg.IMsg;
import com.cvj.msg.IsoMsg;
import com.phx.cvi.comp.mux.IsoMuxObject;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;

/**
 * handling incoming message
 */
@Sharable
@RequiredArgsConstructor
public class TcpHandler<T extends IMsg> extends SimpleChannelInboundHandler<T>{

	private IChannelRegistry channelRegistry;
	private ITcpInterface<T> busInterface;
	private IMux<T> mux;
	
	public TcpHandler(IChannelRegistry iChannelRegistry,ITcpInterface<T> iBusInterface,IMux<T> iMux) {
		this.channelRegistry = iChannelRegistry;
		this.busInterface = iBusInterface;
		this.mux = iMux;
	}
	
	/*on channel connected*/
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		this.channelRegistry.add(ctx.channel());
		this.busInterface.onActiveSession(ctx.channel());
	}
	
	/*on channel isconnected*/
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.channelRegistry.remove(ctx.channel());
		this.busInterface.onInActiveSession(ctx.channel());
	}
	
	protected String constructChannelName(Channel ch) {
		//sementara
		return ch.toString();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
		String channelName = constructChannelName(ctx.channel());
		if(msg.isRequest()) {
			this.busInterface.processRequest(channelName, msg);
		}else {
			IsoMuxObject<T> muxObj = this.mux.getOriginalMsg(msg);
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
