package com.phx.cvi.comp;

import com.cvj.msg.IMsg;
import com.phx.cvi.comp.mux.IsoMuxObject;

public interface IMux<T extends IMsg> {
	public void request(String sourceChannelName,T value, long timeout);
	public void register(String sourceChannelName,T value,long timeout);
	public IsoMuxObject<T> getOriginalMsg(T value);
}
