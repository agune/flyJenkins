package com.agun.flyJenkins.job;


import java.io.File;
import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import com.agun.flyJenkins.model.ServiceGroup;
import com.agun.flyJenkins.persistence.ServiceGroupSaveable;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.JobProperty;
import hudson.model.JobPropertyDescriptor;
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
		System.out.println("build result: " + build.getResult());
		System.out.println("display name : " + build.getDisplayName());
	
		
		try {
			FilePath filePath = new FilePath(new File("/Users/pdc222/work/test.zip"));
			FilePath filePath2 = new FilePath(new File("/Users/pdc222/work/a.war"));
			filePath.unzip(filePath2);
			//FilePath filePath2 = new FilePath(new File(production));
			//filePath2.zip(filePath);
			
			for(FilePath file : build.getWorkspace().list()){
				System.out.println("name :  "  + file.getName());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
