package com.phx.cvi.comp.mux;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.cvj.msg.IsoMsg;
import com.phx.cvi.comp.IMux;
import com.phx.cvi.comp.ISender;

import lombok.extern.slf4j.Slf4j;
/**
 * Handling request and response pair
 */
@Slf4j
public class IsoMux implements IMux<IsoMsg>{
	private Map<String, IsoMuxObject<IsoMsg>> map;
	private BlockingQueue<IsoMuxObject<IsoMsg>> queue;
	private List<Integer> keyFields;

	private ISender<IsoMsg> sender;

	public IsoMux(BlockingQueue<IsoMuxObject<IsoMsg>> iQueue,ISender<IsoMsg> iSender,String rawKeyField) {
		this.queue = iQueue;
		this.sender = iSender;
		this.map  = new HashMap<>();
		StringTokenizer tokenz = new StringTokenizer(rawKeyField);

		this.keyFields = new LinkedList<>();

		tokenz.asIterator().forEachRemaining(v->{
			try {
				keyFields.add(Integer.parseInt(v.toString()));
			}catch(NumberFormatException nfe) {}
		});


	}


	private String constructKey(IsoMsg msg) {
		StringBuilder sb = new StringBuilder();
		this.keyFields.forEach(v->{
			String value = msg.getValue(v);
			if(value!=null)sb.append(value);
		});
		return sb.toString();
	}

	@Override
	public void register(String sourceChannelName,IsoMsg msg, long timeout) {
		String key  = constructKey(msg);

		IsoMuxObject<IsoMsg> obj = new IsoMuxObject<>(key, IsoMuxKey.ST_RQST);
		obj.setSourceChannelName(sourceChannelName);

		map.put(key, obj);

		CompletableFuture<IsoMuxKey> future =  obj.getCompletable();

		IsoMuxKey timeoutKey = new IsoMuxKey(key,IsoMuxKey.ST_TIMEOUT);

		future.completeOnTimeout(timeoutKey, timeout, TimeUnit.MILLISECONDS)
		.thenAccept(muxKey->{
			if(muxKey.getStatus() == IsoMuxKey.ST_TIMEOUT) {
				onTimeout(muxKey);
			}
		});
	}	

	private void onTimeout(IsoMuxKey muxKey) {
		IsoMuxObject<IsoMsg> reqObj = this.map.remove(muxKey.getKey());
		if(reqObj == null) {
			//should not happens
			//just log and done
			log.info("Cannot find timeout key {}", muxKey.getKey());
		}else {
			this.queue.offer(reqObj);
		}
	}

	public IsoMuxObject<IsoMsg> getOriginalMsg(IsoMsg rspMsg) {
		String key = constructKey(rspMsg);

		if(key == null)return null;
		IsoMuxObject<IsoMsg> muxObj = this.map.remove(key);
		if(muxObj == null)return null;
		return muxObj;
	}

	@Override
	public void request(String sourceChannelName, IsoMsg value, long timeout) {
		register(sourceChannelName, value, timeout);
		this.sender.send(sourceChannelName, value);
	}


}
