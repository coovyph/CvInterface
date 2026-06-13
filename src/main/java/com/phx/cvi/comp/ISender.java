package com.phx.cvi.comp;

/**
 * And interface to send message to host
 */
public interface ISender<T> {
	public int send(String channelName,T msg);
	public int send(T msg);
}
