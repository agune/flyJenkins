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
	public final String licenser;
	public final int serverGroup;
	
	@DataBoundConstructor
	public JobExtends(String production, String licenser, int serverGroup){
		this.production = production;
		this.licenser = licenser;
		this.serverGroup = serverGroup;
	}
	
	@Override
	public Action getJobAction(AbstractProject<?, ?> job) {
		return new JobAction(this.owner.getName());
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
	}

}