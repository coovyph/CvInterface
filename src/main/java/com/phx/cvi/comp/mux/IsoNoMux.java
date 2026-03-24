package com.phx.cvi.comp.mux;

import com.cvj.msg.IsoMsg;
import com.phx.cvi.comp.IMux;
import com.phx.cvi.comp.ISender;

/**
 * By pass message to business process
 */
public class IsoNoMux implements IMux<IsoMsg>{
	private ISender sender;


	public IsoNoMux(ISender iSender) {
		this.sender = iSender;
	}
	
	@Override
	public void register(String sourceChannelName, IsoMsg value, long timeout) {
		//do nothing		
	}


	@Override
	public void request(String sourceChannelName, IsoMsg value, long timeout) {
		this.sender.send(sourceChannelName,value);
	}


	@Override
	public IsoMuxObject<IsoMsg> getOriginalMsg(IsoMsg value) {
		// TODO Auto-generated method stub
		return null;
	}

}
