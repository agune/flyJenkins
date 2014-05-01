package org.flyJenkins.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.sf.json.JSONObject;

import org.flyJenkins.rest.model.Production;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Plugin;

public class RestEntryPoint extends Plugin{

	
	public void doProduction(final StaplerRequest req, StaplerResponse rsp){
	
		System.out.println("productionID : " + req.getParameter("productionID"));
		
		
		Production production = new Production();	
		production.setJobName("testJob");
		production.setOutput("/home/flyJenkins/production/test.war");
		production.setCreateDate(new Date());
		production.setBuildNumber(3);
		production.setJobID(2);
		production.setProductionID(1);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("item", production);
		
		try {
			PrintWriter pw = rsp.getWriter();
			pw.write(jsonObject.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}

	
	public void doProductionList(final StaplerRequest req, StaplerResponse rsp){
		
		
		System.out.println("page : " + req.getParameter("page"));
		System.out.println("limit : " + req.getParameter("limit"));
			
		
		
		ArrayList<Production> productionList = new ArrayList<Production>();
		
		
		Production production = new Production();	
		production.setJobName("testJob");
		production.setOutput("/home/flyJenkins/production/test.war");
		production.setCreateDate(new Date());
		production.setBuildNumber(3);
		production.setJobID(2);
		production.setProductionID(1);

		Production production2 = new Production();	
		production2.setJobName("testJob2");
		production2.setOutput("/home/flyJenkins/production2/test.war");
		production2.setCreateDate(new Date());
		production2.setBuildNumber(4);
		production2.setJobID(5);
		production2.setProductionID(6);

		
		Production production3 = new Production();	
		production3.setJobName("testJob2");
		production3.setOutput("/home/flyJenkins/production2/test.war");
		production3.setCreateDate(new Date());
		production3.setBuildNumber(4);
		production3.setJobID(5);
		production3.setProductionID(6);

		
		Production production4 = new Production();	
		production4.setJobName("testJob2");
		production4.setOutput("/home/flyJenkins/production2/test.war");
		production4.setCreateDate(new Date());
		production4.setBuildNumber(4);
		production4.setJobID(5);
		production4.setProductionID(6);

		
		
		productionList.add(production);
		productionList.add(production2);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("totalPage", 10);
		jsonObject.put("itemList", productionList);
		
		try {
			PrintWriter pw = rsp.getWriter();
			pw.write(jsonObject.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void doSaveProduction(final StaplerRequest req, StaplerResponse rsp){
		if(req.getParameter("jobName") == null || req.getParameter("jobName").length() == 0){
			
		}
		
		if(req.getParameter("outPut") == null || req.getParameter("outPut").length() == 0){
			
		}
		
		if(req.getParameter("createDate") == null || req.getParameter("createDate").length() == 0){
			
		}
		
		if(req.getParameter("buildNumber") == null || req.getParameter("buildNumber").length() == 0){
			
		}
		
		if(req.getParameter("jobID") == null || req.getParameter("jobID").length() == 0){
			
		}
		
        if(req.getParameter("productionID") == null || req.getParameter("productionID").length() == 0){
			
		}
		
	}
}
