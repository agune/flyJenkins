package com.agun.jenkins.process;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.agun.agent.model.AgentMeta;
import com.agun.agent.process.CheckRequest;
import com.agun.jenkins.CLIHelper;
import com.agun.jenkins.FilePathHelper;

public class ProcessTest {
	@Ignore
	@Test
	public void peekRequest() {
		URL url  = this.getClass().getResource("/id_rsa");
		CLIHelper cliHelper = new CLIHelper("http://127.0.0.1:8080/jenkins", url.getPath());
		FilePathHelper filePathHelper = new FilePathHelper(cliHelper);
		CheckRequest checkRequest = new CheckRequest(cliHelper, filePathHelper);
		List<AgentMeta> agentList = new ArrayList<AgentMeta>();
		AgentMeta agent = new AgentMeta();
		agent.setPid(789);
		agent.setDestination("/Users/pdc222/app/apache-tomcat-7.0.21");
		agentList.add(agent);
		checkRequest.process(agentList);
		
		cliHelper.destory();
	}

}
