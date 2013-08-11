package com.agun.jenkins;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class ProcessTreeHelperTest {

	/**
	 * 시스템에 종속적이기 때문에 ignore 처리 
	 */
	@Ignore
	@Test
	public void getPrid() {
		int pid = ProcessTreeHelper.getPid("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
	}

}
