package com.phx.cvi.comp;

import java.util.Map;

import com.cvj.msg.IMsg;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * ISO socket service handling
 */
@Slf4j
public class TcpNettySocketAdapter<T extends IMsg> implements ISocketAdapter{
	public final static int ST_SERVER = 1;
	public final static int ST_CLIENT = 0;

	private int socketType;
	private int maxSocketSessions;


	private String host;
	private int port;

	private Map<String,Boolean> tcpOptions; 


	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private Channel hostChannel;
	
	ChannelInitializer<SocketChannel> ch;


	public TcpNettySocketAdapter(ChannelInitializer<SocketChannel> ich) {
		this.ch = ich;
	}

	public void setSocketType(int type) {
		this.socketType = type;
	}
	@Override
	public int getSocketType() {
		return this.socketType;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public void setHost(String host) {
		this.host = host;
	}

	public void setMaxSocketSessions(int socketSessions) {
		this.maxSocketSessions = socketSessions;
	}

	public int getMaxSocketSessions() {
		return maxSocketSessions;
	}

	public void setTcpOptions(Map<String, Boolean> options) {
		this.tcpOptions = options;
	}


	private void initializeServer() {
		this.bossGroup = new NioEventLoopGroup(1);
		this.workerGroup = new NioEventLoopGroup(this.maxSocketSessions);

		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(this.bossGroup, this.workerGroup)
		.channel(NioServerSocketChannel.class)
		.childHandler(this.ch);

		ChannelFuture future = null;
		try{
			future = bootstrap.bind(this.port).sync();
			
			log.info("Host listening at port {}",this.port);
			
		}catch(InterruptedException ie) {
			log.error("InterruptedException occurs ", ie);
			return;
		}
		this.hostChannel = future.channel();
	}

	private void initializeClient() {

		this.workerGroup = new NioEventLoopGroup(this.maxSocketSessions);

		final Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(this.workerGroup)
		.channel(NioSocketChannel.class);

		tcpOptions.forEach((k,v)->{
			bootstrap.option(ChannelOption.valueOf(k), v);
		});

		bootstrap.handler(this.ch);

		ChannelFuture future = null;
		try{
			future = bootstrap.connect(this.host,this.port).sync();
		}catch(InterruptedException ie) {
			log.error("InterruptedException occurs ", ie);
			return;
		}
		this.hostChannel = future.channel();
	}
	@Override
	public void start() {
		// TODO Auto-generated method stub
		if(this.socketType == ST_SERVER) {
			initializeServer();
		}else {
			initializeClient();
		}
	}

	@Override
	public void stop() {
		if(this.hostChannel != null) {
			this.hostChannel.close();
		}

		if(this.bossGroup != null)
			this.bossGroup.shutdownGracefully();

		if(this.workerGroup != null)
			this.workerGroup.shutdownGracefully();
	}

}
