package org.flyJenkins.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.flyJenkins.component.persistent.PersistentDriver;
import org.flyJenkins.component.persistent.PersistentTemplate;
import org.flyJenkins.model.Production;
import org.flyJenkins.model.ServiceGroup;
import org.flyJenkins.model.ServiceMeta;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Plugin;

public class RestEntryPoint extends Plugin{

	
	public void doInitDB(final StaplerRequest req, StaplerResponse rsp){
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		PersistentDriver pv = persistentTemplate.getPersistentDriver();
		pv.initSchema();
	}
	
	public void doProduction(final StaplerRequest req, StaplerResponse rsp){		
		PrintWriter pw = null;
		JSONObject jsonObject = new JSONObject();
			
		try {
			pw = rsp.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(req.getParameter("productionID") == null){
			jsonObject.put("status", "error");
			jsonObject.put("message", "productionID is null");
			pw.write(jsonObject.toString());
			return;
		}
				
		int productionID = Integer.parseInt(req.getParameter("productionID"));
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		Production production = new Production();
		production.setProductionID(productionID);		
		production = persistentTemplate.getRead(production, Production.class);
		jsonObject.put("item", production);
		pw.write(jsonObject.toString());
	}

	
	public void doProductionList(final StaplerRequest req, StaplerResponse rsp){
		
		
		System.out.println("page : " + req.getParameter("page"));
		System.out.println("limit : " + req.getParameter("limit"));
		
		int page  = (req.getParameter("page") == null)? 1 : Integer.parseInt(req.getParameter("page"));
		int limit  = (req.getParameter("limit") == null)? 10 : Integer.parseInt(req.getParameter("limit"));
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		int totalPage = persistentTemplate.getTotalPage(new Production(), limit, Production.class);
		List<Production> productionList = persistentTemplate.getPageList(page, limit, Production.class);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("totalPage", totalPage);
		jsonObject.put("itemList", productionList);
		try {
			PrintWriter pw = rsp.getWriter();
			pw.write(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doSaveProduction(final StaplerRequest req, StaplerResponse rsp){
		
	    JSONObject jsonObject = new JSONObject();
		PrintWriter pw = null;
		try {
			pw = rsp.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(req.getParameter("jobName") == null || req.getParameter("jobName").length() == 0){
			jsonObject.put("status", "error");
			jsonObject.put("message", "jobName is null or empty ");
			pw.write(jsonObject.toString());
			return;
		}
		
		if(req.getParameter("outPut") == null || req.getParameter("outPut").length() == 0){
			jsonObject.put("status", "error");
			jsonObject.put("message", "outPut is null or empty ");
			pw.write(jsonObject.toString());
			return;
		}
				
		if(req.getParameter("buildNumber") == null || req.getParameter("buildNumber").length() == 0){
			jsonObject.put("status", "error");
			jsonObject.put("message", "buildNumber is null or empty ");
			pw.write(jsonObject.toString());
			return;
		}
		
		if(req.getParameter("jobID") == null || req.getParameter("jobID").length() == 0){
			jsonObject.put("status", "error");
			jsonObject.put("message", "jobID is null or empty ");
			pw.write(jsonObject.toString());
			return;
		}
		
        if(req.getParameter("productionID") == null || req.getParameter("productionID").length() == 0){
			jsonObject.put("status", "error");
			jsonObject.put("message", "ProductionID is null or empty ");
			pw.write(jsonObject.toString());
			return;
		}
        
        Production prodction = new Production();
        prodction.setJobName(req.getParameter("jobName"));
        prodction.setBuildNumber(Integer.parseInt(req.getParameter("buildNumber")));
        prodction.setJobID(Integer.parseInt(req.getParameter("jobID")));
        prodction.setProductionID(Integer.parseInt(req.getParameter("productionID")));
        prodction.setOutput(req.getParameter("outPut"));
        prodction.setCreateDate(new Date());
        
        PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
        persistentTemplate.query(prodction);
        
    	jsonObject.put("status", "ok");
		pw.write(jsonObject.toString());
	}
	
	
	
	public void doService(final StaplerRequest req, StaplerResponse rsp){		
		PrintWriter pw = null;
		JSONObject jsonObject = new JSONObject();
			
		try {
			pw = rsp.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(req.getParameter("serviceID") == null){
			jsonObject.put("status", "error");
			jsonObject.put("message", "serviceID is null");
			pw.write(jsonObject.toString());
			return;
		}
				
		int serviceID = Integer.parseInt(req.getParameter("serviceID"));
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		ServiceMeta serviceMeta = new ServiceMeta();
		serviceMeta.setServiceID(serviceID);		
		serviceMeta = persistentTemplate.getRead(serviceMeta, ServiceMeta.class);
		jsonObject.put("item", serviceMeta);
		pw.write(jsonObject.toString());
	
	}
	
	public void doServiceList(final StaplerRequest req, StaplerResponse rsp){		
		
		System.out.println("page : " + req.getParameter("page"));
		System.out.println("limit : " + req.getParameter("limit"));
		
		int page  = (req.getParameter("page") == null)? 1 : Integer.parseInt(req.getParameter("page"));
		int limit  = (req.getParameter("limit") == null)? 10 : Integer.parseInt(req.getParameter("limit"));
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		int totalPage = persistentTemplate.getTotalPage(new ServiceMeta(), limit, ServiceMeta.class);

		List<ServiceMeta> serviceMetaList = persistentTemplate.getPageList(page, limit, ServiceMeta.class);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("totalPage", totalPage);
		jsonObject.put("itemList", serviceMetaList);
		try {
			PrintWriter pw = rsp.getWriter();
			pw.write(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	
	public void doSaveService(final StaplerRequest req, StaplerResponse rsp){		
	    JSONObject jsonObject = new JSONObject();
		PrintWriter pw = null;
		try {
			pw = rsp.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(req.getParameter("groupID") == null || req.getParameter("groupID").length() == 0){
			jsonObject.put("status", "error");
			jsonObject.put("message", "groupID is null or empty ");
			pw.write(jsonObject.toString());
			return;
		}
		
		if(req.getParameter("type") == null || req.getParameter("type").length() == 0){
			jsonObject.put("status", "error");
			jsonObject.put("message", "type is null or empty ");
			pw.write(jsonObject.toString());
			return;
		}
		
		if(req.getParameter("weight") == null || req.getParameter("weight").length() == 0){
			jsonObject.put("status", "error");
			jsonObject.put("message", "weight is null or empty ");
			pw.write(jsonObject.toString());
			return;
		}

		if(req.getParameter("host") == null || req.getParameter("host").length() == 0){
			jsonObject.put("status", "error");
			jsonObject.put("message", "host is null or empty ");
			pw.write(jsonObject.toString());
			return;
		}

		//group_id, destination, service_type, command, weight, test_url
		
		ServiceMeta serviceMeta = new ServiceMeta();
		serviceMeta.setGroupID(Integer.parseInt(req.getParameter("groupID")));
		serviceMeta.setDestination(req.getParameter("destination"));
		serviceMeta.setType(Integer.parseInt(req.getParameter("type")));
		serviceMeta.setCommand(req.getParameter("command"));
		serviceMeta.setWeight(Integer.parseInt(req.getParameter("weight")));
		serviceMeta.setTestUrl(req.getParameter("testURL"));
		serviceMeta.setHost(req.getParameter("host"));
		
	    PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
        persistentTemplate.query(serviceMeta);
    	jsonObject.put("status", "ok");
		pw.write(jsonObject.toString());
		
	}

	
	
	
	
	public void doServiceGroup(final StaplerRequest req, StaplerResponse rsp){		
		PrintWriter pw = null;
		JSONObject jsonObject = new JSONObject();
			
		try {
			pw = rsp.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(req.getParameter("groupID") == null){
			jsonObject.put("status", "error");
			jsonObject.put("message", "groupID is null");
			pw.write(jsonObject.toString());
			return;
		}
				
		int serviceGroupID = Integer.parseInt(req.getParameter("groupID"));
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		ServiceGroup serviceGroup = new ServiceGroup();
		serviceGroup.setGroupID(serviceGroupID);		
		serviceGroup = persistentTemplate.getRead(serviceGroup, ServiceGroup.class);
		jsonObject.put("item", serviceGroup);
		pw.write(jsonObject.toString());
	
	}
	
	public void doSaveServiceGroup(final StaplerRequest req, StaplerResponse rsp){		
	    JSONObject jsonObject = new JSONObject();
		PrintWriter pw = null;
		try {
			pw = rsp.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(req.getParameter("groupName") == null || req.getParameter("groupName").length() == 0){
			jsonObject.put("status", "error");
			jsonObject.put("message", "groupName is null or empty ");
			pw.write(jsonObject.toString());
			return;
		}
		
		ServiceGroup serviceGroup = new ServiceGroup();
		serviceGroup.setName(req.getParameter("groupName"));
		
	    PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
        persistentTemplate.query(serviceGroup);
    	jsonObject.put("status", "ok");
		pw.write(jsonObject.toString());
		
	}
	
	public void doServiceGroupList(final StaplerRequest req, StaplerResponse rsp){		
		
		System.out.println("page : " + req.getParameter("page"));
		System.out.println("limit : " + req.getParameter("limit"));
		
		int page  = (req.getParameter("page") == null)? 1 : Integer.parseInt(req.getParameter("page"));
		int limit  = (req.getParameter("limit") == null)? 10 : Integer.parseInt(req.getParameter("limit"));
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		int totalPage = persistentTemplate.getTotalPage(new ServiceGroup(), limit, ServiceGroup.class);

		List<ServiceGroup> serviceGroupList = persistentTemplate.getPageList(page, limit, ServiceGroup.class);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("totalPage", totalPage);
		jsonObject.put("itemList", serviceGroupList);
		try {
			PrintWriter pw = rsp.getWriter();
			pw.write(jsonObject.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
