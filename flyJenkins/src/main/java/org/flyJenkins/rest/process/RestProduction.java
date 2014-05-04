package org.flyJenkins.rest.process;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.flyJenkins.component.persistent.PersistentTemplate;
import org.flyJenkins.model.Production;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

public class RestProduction {
	
	public void execProduction(final StaplerRequest req, StaplerResponse rsp){		
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

	
	public void execProductionList(final StaplerRequest req, StaplerResponse rsp){
		
		
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

	public void execSaveProduction(final StaplerRequest req, StaplerResponse rsp){
		
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

}
