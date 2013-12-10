/**
 * This code is learning code for JenkinsRule
 * @author agun
 * @see org.jvnet.hudson.test.JenkinsRule
 */
package org.flyJenkins.jenkins;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.tasks.Shell;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;


public class JenkinsRuleTest {
	@Rule 
	public JenkinsRule jenkinsRule = new JenkinsRule();
	
	@Test
	public void createProjectTest() {
		try {
			FreeStyleProject project = jenkinsRule.createFreeStyleProject();
			project.getBuildersList().add(new Shell("echo hello"));
			
			FreeStyleBuild build = project.scheduleBuild2(0).get();
			
			String s = FileUtils.readFileToString(build.getLogFile());
			assertTrue("fail echo!!", s.contains("+ echo hello"));
			
		} catch (IOException e) {
			assertTrue(e.getMessage(), false);
		} catch (InterruptedException e) {
			assertTrue(e.getMessage(), false);
		} catch (ExecutionException e) {
			assertTrue(e.getMessage(), false);
		}
	}

}
