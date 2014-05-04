package org.flyJenkins.rest;

import org.flyJenkins.component.persistent.PersistentDriver;
import org.flyJenkins.component.persistent.PersistentTemplate;
import org.flyJenkins.rest.process.RestProduction;
import org.flyJenkins.rest.process.RestService;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Plugin;

public class RestEntryPoint extends Plugin{

	
	private RestService restService = null;
	private RestProduction restProduction = null;
	
	public RestEntryPoint(){
		restService = new RestService();
		restProduction = new RestProduction();
	}
	
	public void doInitDB(final StaplerRequest req, StaplerResponse rsp){
		PersistentTemplate persistentTemplate = PersistentTemplate.getInstance("org.flyJenkins.component.persistent.hsql.HSQLDriver");
		PersistentDriver pv = persistentTemplate.getPersistentDriver();
		pv.initSchema();
	}
	
	/**
	 * implement production start 
	 */
	
	public void doProduction(final StaplerRequest req, StaplerResponse rsp){		
		restProduction.execProduction(req, rsp);
	}

	
	public void doProductionList(final StaplerRequest req, StaplerResponse rsp){
		restProduction.execProductionList(req, rsp);
	}

	public void doSaveProduction(final StaplerRequest req, StaplerResponse rsp){
		restProduction.execSaveProduction(req, rsp);
	}
	
	
	/**
	 * implement service start 
	 */
	public void doService(final StaplerRequest req, StaplerResponse rsp){		
		restService.execService(req, rsp);
	}
	
	public void doServiceList(final StaplerRequest req, StaplerResponse rsp){		
		restService.execServiceList(req, rsp);
	}
	
	public void doSaveService(final StaplerRequest req, StaplerResponse rsp){		
		restService.execSaveService(req, rsp);
	}
	
	
	/**
	 * implement service group start 
	 */
	public void doServiceGroup(final StaplerRequest req, StaplerResponse rsp){		
		restService.execServiceGroup(req, rsp);
	}
	
	public void doSaveServiceGroup(final StaplerRequest req, StaplerResponse rsp){		
		restService.execSaveServiceGroup(req, rsp);
	}
	
	public void doServiceGroupList(final StaplerRequest req, StaplerResponse rsp){		
		restService.execServiceGroupList(req, rsp);
	}
}
