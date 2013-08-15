package com.agun.flyJenkins.job;


import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import hudson.Extension;
import hudson.model.Action;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
import hudson.model.AbstractProject;

public class JobExtends extends JobProperty<AbstractProject<?,?>>  {
	public final String production;
	public final String deployer;
	
	@DataBoundConstructor
	public JobExtends(String production, String deployer){
		this.production = production;
		this.deployer = deployer;
	}
	
	@Override
	public Action getJobAction(AbstractProject<?, ?> job) {
		System.out.println("======> get action test");
		
		return new JobAction();
	}

	
	@Extension
	public static final class DescriptorImpl extends JobPropertyDescriptor {
		
		public DescriptorImpl(){
			super(JobExtends.class);
			load();
		}
		
		@Override
		public String getDisplayName() {
			return "DeployRequest";
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
		
	}

}
