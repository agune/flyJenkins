package com.agun.agent;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class flyAgentTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void doStartTest() {
		flyAgent agent = new flyAgent();
		agent.doStart(null);
		try {
			agent.start();
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(e.getMessage(), true);
		}
	}

}
