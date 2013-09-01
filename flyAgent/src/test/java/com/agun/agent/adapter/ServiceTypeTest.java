package com.agun.agent.adapter;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.agun.agent.model.AgentMeta;
import com.agun.jenkins.CLIHelper;
import com.agun.jenkins.FilePathHelper;
/**
 * 서비스 타입의 기능을 테스트 한다.
 * 
 * @author agun
 *
 */
public class ServiceTypeTest {

	@Ignore
	@Test
	public void tomcatServiceTest() {
	
		URL url  = this.getClass().getResource("/id_rsa");
		CLIHelper cliHelper = new CLIHelper("http://127.0.0.1:8080/jenkins", url.getPath());
		Map<String, Object> deployInfoMap = (Map<String, Object>) cliHelper.callActionFunction("flyJenkins", "getProduction", 1);
		System.out.println(deployInfoMap);
		
		assertTrue("jenkins에 해당 agnet에 대한 production 정보가 없습니다.", deployInfoMap.containsKey("production"));
		
		
		String production =  (String) deployInfoMap.get("production");
		
		AgentMeta agentMeta = new AgentMeta();
		agentMeta.setId(1);
		
		FilePathHelper filePathHelper = new FilePathHelper(cliHelper);
		TomcatService serviceType = new TomcatService();
		serviceType.setFilePathHelper(filePathHelper);
		boolean okProduction = serviceType.getProduction(agentMeta, production);
	
		assertTrue("production을 복사 하지 못했습니다.", okProduction);
		
		cliHelper.destory();
	}
	
	@Ignore
	@Test
	public void tomcatStopTest(){
		AgentMeta agentMeta = new AgentMeta();
		agentMeta.setDestination("E:\\apache-tomcat-6.0.32");
		
		TomcatService tomcatService = new TomcatService();
		tomcatService.stop(agentMeta);
		
	}
	
	@Ignore
	@Test
	public void tomcatStartTest(){
		AgentMeta agentMeta = new AgentMeta();
		agentMeta.setDestination("E:\\apache-tomcat-6.0.32");
		
		TomcatService tomcatService = new TomcatService();
		tomcatService.start(agentMeta);
	}
	@Ignore
	@Test
	public void tomcatGetProductionTest(){
		AgentMeta agentMeta = new AgentMeta();
		agentMeta.setDestination("/Users/pdc222/app/apache-tomcat-7.0.21");
		agentMeta.setServiceId(1);
		agentMeta.setId(1);
		TomcatService serviceType = new TomcatService();
		serviceType.getProduction(agentMeta, "/Users/pdc222/work/text.xml");
	}
}
