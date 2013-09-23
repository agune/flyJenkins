package com.agun.agent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class flyAgentTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Ignore
	@Test
	public void doStartTest() {
		flyAgent agent = new flyAgent();
		agent.setAgentHost("127.0.0.1");
		String[] args = new String[2];
		args[0] = "127.0.0.1:8080/jenkins";
		args[1] = "E:\\flyJenkins\\flyAgent\\src\\test\\resources\\id_rsa";
		agent.doStart(args);
		try {
			agent.start();
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(e.getMessage(), true);
		}
	}

}
