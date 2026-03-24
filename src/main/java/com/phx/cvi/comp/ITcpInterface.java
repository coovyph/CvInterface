package com.phx.cvi.comp;

import com.cvj.msg.IMsg;

import io.netty.channel.Channel;

public interface ITcpInterface<T extends IMsg> {

	public void onActiveSession(Channel ch);
	public void onInActiveSession(Channel ch);
	public void processRequest(String reqChannelName,T msg);
	public void processResponse(String reqChannelName,String rspChannelName,T reqMsg,T msg);
	public void onTimeout(String reqChannelName,T msg);
}
