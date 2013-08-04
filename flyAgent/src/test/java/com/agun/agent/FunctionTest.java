package com.agun.agent;

import static org.junit.Assert.*;
import hudson.util.ProcessTree;
import hudson.util.ProcessTree.OSProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class FunctionTest {

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
		
		/*
		try {
			Process p = Runtime.getRuntime().exec("ps aux");
			p.getInputStream();
			String line;
			
			
			BufferedReader input =
	                new BufferedReader(new InputStreamReader(p.getInputStream()));
	        while ((line = input.readLine()) != null) {
	            System.out.println(line); //<-- Parse data here.
	        }
	        input.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	
	}

}
