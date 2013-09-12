package com.agun.flyJenkins.job;


import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.agun.flyJenkins.deploy.DeployProduction;
import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.persistence.ServiceGroupSaveable;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.Result;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

public class JobExtends extends JobProperty<AbstractProject<?,?>>  {
	public final String production;
	public final String licenser;
	public final int serviceGroup;
	
	@DataBoundConstructor
	public JobExtends(String production, String licenser, int serviceGroup){
		this.production = production;
		this.licenser = licenser;
		this.serviceGroup = serviceGroup;
		System.out.println("===> start : " + serviceGroup);
	}
	
	@Override
	public Action getJobAction(AbstractProject<?, ?> job) {
		return new JobAction(this.owner.getName());
	}
	
	@Override
	public boolean perform(AbstractBuild<?,?> build, Launcher launcher, BuildListener listener){
		Result result = build.getResult();
		String replaceProduction = null;
		if(production != null){
			replaceProduction = production.replaceAll("[$]\\{workspace\\}", build.getWorkspace().getRemote());
		}
		// check state of building
		if(DeployProduction.isSuccess(result.toString())){
			
			System.out.println("======> start : ");
			DeployProduction deployProduction = new DeployProduction();
			deployProduction.process(this.owner.getName(), replaceProduction, serviceGroup, build);
		}
		return true;
	}
	
	@Extension
	public static final class DescriptorImpl extends JobPropertyDescriptor {
		
		public DescriptorImpl(){
			super(JobExtends.class);
			load();
		}
		
		@Override
		public String getDisplayName() {
			return "Deploy Request";
		}
		
		@Override
		public JobProperty<?> newInstance(StaplerRequest req,
				JSONObject formData) throws FormException {

			if (formData.isEmpty()) {
				return null;
			}
			JobExtends bpp = req.bindJSON(
					JobExtends.class,
					formData.getJSONObject("flyRequest"));
			return bpp;
		}
		
		public List<ServiceGroup> getServiceGroupList(){
			ServiceGroupSaveable serviceGroupSaveable = new ServiceGroupSaveable();
			serviceGroupSaveable.load();
			return serviceGroupSaveable.getServiceGroupList();
		}
	}

}
