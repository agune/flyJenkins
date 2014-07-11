package org.flyJenkins.rest.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JSONObject;

import org.flyJenkins.component.persistent.PersistentTemplate;
import org.flyJenkins.model.ServiceGroup;
import org.flyJenkins.model.ServiceMeta;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class RestService {
	
	
	/***
	 * implement start service
	 */
	public void execService(final StaplerRequest req, StaplerResponse rsp){		
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
		
		try {
		    String callbackName = req.getParameter("callback");
			if(callbackName == null)
				jsonObject.write(pw);
			else
				pw.write(callbackName + "(" +jsonObject.toString() + ")" );
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void execServiceList(final StaplerRequest req, StaplerResponse rsp){		
		
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

	public void execDelService(final StaplerRequest req, StaplerResponse rsp){
		JSONObject jsonObject = new JSONObject();	
		PrintWriter pw = null;
		
		try {
			pw = rsp.getWriter();
			if(req.getParameter("serviceID") == null){
				jsonObject.put("status", "error");
				jsonObject.put("message", "serviceID is null");
				jsonObject.write(pw);
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		ServiceMeta serviceMeta = new ServiceMeta();
		serviceMeta.setServiceID(Integer.parseInt(req.getParameter("serviceID")));
		persistentTemplate.del(serviceMeta, ServiceMeta.class);
		
		if(req.getParameter("returnURL") != null){
  		  try {
  			rsp.sendRedirect2(req.getParameter("returnURL"));
  		  } catch (IOException e) {
  			e.printStackTrace();
  		  }
  		  return;
  		}
					
		try {
			jsonObject.put("status", "ok");		
			String callbackName = req.getParameter("callback");
			if(callbackName != null)
			  pw.write(callbackName + "(" +jsonObject.toString() + ")" );
			else
			  jsonObject.write(pw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void execSaveService(final StaplerRequest req, StaplerResponse rsp){		
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
		
		System.out.println("test URL =======> : " + req.getParameter("testURL"));
		//group_id, destination, service_type, command, weight, test_url
		
		ServiceMeta serviceMeta = new ServiceMeta();
		serviceMeta.setGroupID(Integer.parseInt(req.getParameter("groupID")));
		serviceMeta.setDestination(req.getParameter("destination"));
		serviceMeta.setType(Integer.parseInt(req.getParameter("type")));
		serviceMeta.setCommand(req.getParameter("command"));
		serviceMeta.setWeight(Integer.parseInt(req.getParameter("weight")));
		serviceMeta.setTestUrl(req.getParameter("testURL"));
		serviceMeta.setHost(req.getParameter("host"));
		
		if(req.getParameter("serviceID") != null && req.getParameter("serviceID").equals("0") == false)
			serviceMeta.setServiceID(Integer.parseInt(req.getParameter("serviceID")));
		
	    PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
        persistentTemplate.query(serviceMeta);
    	
        if(req.getParameter("returnURL") != null){
  		  try {
  			rsp.sendRedirect2(req.getParameter("returnURL"));
  		  } catch (IOException e) {
  			e.printStackTrace();
  		  }
  		  return;
  		}
        
        try {
			jsonObject.write(pw);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	
	/**
	 * implement start service group 
	 */
	
	public void execServiceGroup(final StaplerRequest req, StaplerResponse rsp){		
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
		
		try {
		    String callbackName = req.getParameter("callback");
			if(callbackName != null)
			  pw.write(callbackName + "(" +jsonObject.toString() + ")" );
			else
			  jsonObject.write(pw);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void execSaveServiceGroup(final StaplerRequest req, StaplerResponse rsp){		
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
		if(req.getParameter("groupID") != null && req.getParameter("groupID").equals("0") == false)
			serviceGroup.setGroupID(Integer.parseInt(req.getParameter("groupID")));
		
	    PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
        persistentTemplate.query(serviceGroup);
    	
        if(req.getParameter("returnURL") != null){
        	try {
        		rsp.sendRedirect2(req.getParameter("returnURL"));
        	} catch (IOException e) {
        		e.printStackTrace();
    		}
        	return;
    	}
        
        jsonObject.put("status", "ok");
        try {
		    String callbackName = req.getParameter("callback");
			System.out.println("callbackName ======>" + callbackName);
		    
		    if(callbackName != null)
			  pw.write(callbackName + "(" +jsonObject.toString() + ")" );
			else
			  jsonObject.write(pw);			
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
	
	public void execDelGroup(final StaplerRequest req, StaplerResponse rsp){
		JSONObject jsonObject = new JSONObject();	
		PrintWriter pw = null;
		
		try {
			pw = rsp.getWriter();
			if(req.getParameter("groupID") == null){
				jsonObject.put("status", "error");
				jsonObject.put("message", "group id is null");
				jsonObject.write(pw);
				return;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		ServiceGroup serviceGroup =  new ServiceGroup();
		serviceGroup.setGroupID(Integer.parseInt(req.getParameter("groupID")));
		persistentTemplate.del(serviceGroup, ServiceGroup.class);
		
		if(req.getParameter("returnURL") != null){
  		  try {
  			rsp.sendRedirect2(req.getParameter("returnURL"));
  		  } catch (IOException e) {
  			e.printStackTrace();
  		  }
  		  return;
  		}
					
		try {
			jsonObject.put("status", "ok");		
			String callbackName = req.getParameter("callback");
			if(callbackName != null)
			  pw.write(callbackName + "(" +jsonObject.toString() + ")" );
			else
			  jsonObject.write(pw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void execServiceGroupList(final StaplerRequest req, StaplerResponse rsp){		
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
		    String callbackName = req.getParameter("callback");
			PrintWriter pw = rsp.getWriter();
			if(callbackName != null)
			  pw.write(callbackName + "(" +jsonObject.toString() + ")" );
			else
			  jsonObject.write(pw);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
