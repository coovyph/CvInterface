package com.phx.cvi.comp;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.cvj.msg.IMsg;
import com.phx.cvi.comp.mux.IsoMuxObject;


public class TimeoutInterfaceManager<T extends IMsg> implements Runnable{
	private BlockingQueue<IsoMuxObject<T>> queue;
	private IBusInterface<T> busInterface;
	private Thread t;
	private boolean running = false;

	public TimeoutInterfaceManager(IBusInterface<T> iBusInterface,BlockingQueue<IsoMuxObject<T>> iQueue) {
		this.busInterface = iBusInterface;
		this.queue = iQueue;
	}

	public void start() {
		t = new Thread(this);
		this.running = true;
		t.start();
	}

	public void stop(){
		this.running = false;
		t.interrupt();
	}

	@Override
	public void run() {
		IsoMuxObject<T> to;
		while(this.running) {
			to = null;
			try {
				to=queue.poll(1000, TimeUnit.MILLISECONDS);
			}catch(InterruptedException ie) {
				//do nothing
			}
			if(to!=null) {
				busInterface.onTimeout(to.getSourceChannelName(), to.getObj());
			}
		}

	}


}
