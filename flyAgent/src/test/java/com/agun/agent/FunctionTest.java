package com.agun.agent;

import static org.junit.Assert.*;
import hudson.util.ProcessTree;
import hudson.util.ProcessTree.OSProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.stapler.Stapler;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class FunctionTest {

	@Ignore
	@Test
	public void test() {
		ProcessTree pstree =  ProcessTree.get();
		Iterator<OSProcess> processIter =    pstree.iterator();
		while(processIter.hasNext()){
			
			OSProcess osProcess = processIter.next();
			
			List<String> argList = osProcess.getArguments();
			int i = 0;
			if(argList.size() > 0)
			System.out.println("===> 0 : " + argList.get(0));
			
			for(String arg : argList){
				System.out.println("arg " + i + arg);
				break;
			}
			
		}
	}
	
	@Ignore
	@Test
	public void testHttpClient(){
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://www.miclub.com");
		
		try {
			HttpResponse response1 =  httpClient.execute(httpGet);
			StatusLine statusLine = response1.getStatusLine();
			
			int statusCode = statusLine.getStatusCode();
			
			System.out.println("status code ;" + statusCode);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Ignore
	@Test
	public void getEnvTest(){
		String agentHome = System.getenv("FLY_AGENT_HOME");
		System.out.println("==> " + agentHome);
	}
	
	@Ignore
	@Test
	public void log4jTest(){
		Logger log = Logger.getLogger(FunctionTest.class.getName());
		log.info("test");
	}

}
