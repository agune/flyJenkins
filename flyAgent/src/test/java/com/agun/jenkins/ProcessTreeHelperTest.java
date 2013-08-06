package com.agun.jenkins;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProcessTreeHelperTest {


	@Test
	public void getPrid() {
		ProcessTreeHelper.getPid("test.exe");
	}

}
