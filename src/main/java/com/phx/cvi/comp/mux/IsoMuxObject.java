package com.phx.cvi.comp.mux;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import com.cvj.msg.IMsg;

public class IsoMuxObject<T extends IMsg> {
	private IsoMuxKey key;
	private CompletableFuture<IsoMuxKey> completable;
	private String sourceChannelName;
	private T obj;

	public IsoMuxObject(String ikey, int status) {
		setKey(new IsoMuxKey(ikey, status));
		this.completable = new CompletableFuture<>();

	}

	public void setSourceChannelName(String sourceChannelName) {
		this.sourceChannelName = sourceChannelName;
	}

	public String getSourceChannelName() {
		return sourceChannelName;
	}

	public void setKey(IsoMuxKey key) {
		this.key = key;
	}

	public IsoMuxKey getKey() {
		return key;
	}

	public CompletableFuture<IsoMuxKey> getCompletable() {
		return completable;
	}
	public T getObj() {
		return obj;
	}
	public void setObj(T obj) {
		this.obj = obj;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IsoMuxObject other = (IsoMuxObject) obj;
		return Objects.equals(key, other.key);
	}




}
