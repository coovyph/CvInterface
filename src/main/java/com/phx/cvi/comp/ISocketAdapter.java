package com.phx.cvi.comp;

/**
 * Handling incoming / outgoing message on TCP/IP
 */
public interface ISocketAdapter {
   public int getSocketType();
   
   public void start();
   public void stop();
}
