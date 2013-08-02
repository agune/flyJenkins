/**
 * flyJenkins 의 flyAgent 를 구현
 * @author agun
 */
package com.agun.agent;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

public class flyAgent implements Daemon{

	private Thread thread;
	private boolean stoped= false;
	private boolean lastOneWasTick = false;
	
	
	@Override
	public void destroy() {
		thread = null;
	}

	@Override
	public void init(DaemonContext daemonContext) throws DaemonInitException, Exception {
		String[] args =  daemonContext.getArguments();
		doStart(args);
	}

	public void doStart(String[] args){
		thread = new Thread(){
			private long lastTick =  0;
			
			@Override
			public synchronized void start(){
				
				while(!stoped){
					long now = System.currentTimeMillis();
					if(now - lastTick >= 1000){
						System.out.println(!lastOneWasTick ? "tick" : "tock");
						lastOneWasTick = !lastOneWasTick;
						lastTick = now;
					}
				}
			}
		};
		
	}
	@Override
	public void start() throws Exception {
		thread.start();
	}

	public void stop() throws Exception {
			stoped = true;
			try {
				thread.join();
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
				throw e;
			}
	}
}
