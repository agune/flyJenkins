package com.agun.flyJenkins.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.model.DeployRequest;
import com.agun.flyJenkins.model.ProductionMeta;
import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.job.JobExtends;
import com.agun.flyJenkins.persistence.DeployRequestSaveable;
import com.agun.flyJenkins.persistence.ProductionSaveable;
import com.agun.flyJenkins.persistence.ServiceGroupSaveableUtil;
import com.agun.flyJenkins.ui.ConfigServiceMeta.DescriptorImpl;
import com.agun.flyJenkins.util.AjaxProxy;

import hudson.Extension;
import hudson.model.User;

@Extension
public class DeployInfo extends FlyUI {

	public DeployInfo(){
	
	}
	
	@Override
	public String getDescription() {
		return "you can see request deploy info";
	}

	
	/**
     * request 한 데이터를 저장한다.
     * @param request
     * @param response
     */
    public void doSave(final StaplerRequest request, final 
    		StaplerResponse response) { 
    
    	if( request.getParameter("jobName") == null 
    			|| request.getParameter("buildNumber") ==null)
    		return;
    	
    	String jobName  = request.getParameter("jobName");
    	int buildNumber = Integer.parseInt(request.getParameter("buildNumber"));
    	
    	Map<String, Object> resultMap = FlyFactory.getPropertiesOfJob(jobName);
    	if(resultMap.size() == 0)
    		return;
    	
    	String licenser = "";
    	String displayProduction = "";
    	int serverGroup = 0;
    	for(Entry entry : resultMap.entrySet()){
    		if(entry.getValue() instanceof JobExtends){
    			JobExtends jobExtend = (JobExtends) entry.getValue();
    			licenser  = jobExtend.licenser;
    			serverGroup =  jobExtend.serviceGroup;
    			displayProduction = jobExtend.production;
    			break;
    		}
    	}
    	User user = User.current();
     	if(user == null)
    		return;
   
     	
     	ProductionSaveable productionSaveable = new ProductionSaveable();
     	productionSaveable.load();
     	
     	List<ProductionMeta> productionMetaList = productionSaveable.getProductionMetaList();
     	if(productionMetaList == null)
     		return;
     	
     	String production= null;
     	
     	for(ProductionMeta productionMeta : productionMetaList){
     		if(productionMeta.getJobName().equals(jobName) && productionMeta.getBuildNumber() == buildNumber){
     			production = productionMeta.getProductionPathOfJob();
     			break;
     		}
     	}
     	
     	if(production == null){ return; }
     	
    	String usedId = user.getId();
    	DeployRequest deployRequest = new DeployRequest();
    	deployRequest.setDate(new Date());
    	deployRequest.setJobName(jobName);
    	deployRequest.setLicenser(licenser);
    	deployRequest.setProduction(production);
    	deployRequest.setBuildNumber(buildNumber);
    	deployRequest.setRequester(usedId);
    	deployRequest.setServerGroup(serverGroup);
    	deployRequest.setDisplayProduction(displayProduction);
    	
    	DeployRequestSaveable deployRequestSaveable = new DeployRequestSaveable();
    	deployRequestSaveable.load();
    	List<DeployRequest> deployRequestList = deployRequestSaveable.getDeployRequestList();
    	
    	if(deployRequestList == null)
    		deployRequestList = new ArrayList<DeployRequest>();
    	
    	deployRequestList.add(deployRequest);
    	deployRequestSaveable.setDeployRequestList(deployRequestList);
    	
    	try {
    		deployRequestSaveable.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	try {
    		PrintWriter printWriter = response.getWriter();
    		printWriter.write("ok");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    public List<DeployRequest> getDeployRequestList(String jobName){
    	
    	DeployRequestSaveable deployRequestSaveable = new DeployRequestSaveable();
    	deployRequestSaveable.load();
    	
    	List<DeployRequest> saveDeployRequestList = deployRequestSaveable.getDeployRequestList();
    	if(saveDeployRequestList == null)
    		return Collections.EMPTY_LIST;
    	
    	if(jobName == null)
    		return saveDeployRequestList;
    	
    	List<DeployRequest> deployRequestList = new ArrayList<DeployRequest>();
    	for(DeployRequest request : saveDeployRequestList){
    		if(request.getJobName().equals(jobName))
    			deployRequestList.add(request);
    	}
    	return deployRequestList;
    }
    
	public String getGroupName(int groupId){
		ServiceGroup serviceGroup = ServiceGroupSaveableUtil.getServiceGroup(groupId);
		if(serviceGroup == null)
			return null;
		return serviceGroup.getGroupName();
	}
 
    public AjaxProxy getAjaxProxy(){
 		return AjaxProxy.getInstance();
 	}
   
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }
    
	@Extension
	 public static class DescriptorImpl extends FlyUIDescriptor {
	
		private String jobName;
		
		
		 @Override
	     public String getDisplayName() {
			 return "";
	     }
	 }
}
