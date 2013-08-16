package com.agun.flyJenkins.user;

import net.sf.json.JSONObject;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.export.Exported;

import hudson.Extension;
import hudson.model.UserProperty;
import hudson.model.UserPropertyDescriptor;
import hudson.model.User;

public class FlyUser extends UserProperty {
	private final boolean isFlyRoot;
	
	public FlyUser(boolean isFlyRoot){
		this.isFlyRoot = isFlyRoot;
	}

	@Exported
	public boolean getIsFlyRoot(){
		return this.isFlyRoot;
	}
	
	public static boolean isFlyRoot(){
		User user = User.current();
		boolean flyRoot = false;
		if(user!=null && user.getProperty(FlyUser.class).getIsFlyRoot()){
			flyRoot=true;
	    }
		return flyRoot;
	}
	
	 @Extension
	  public static final class DescriptorImpl extends UserPropertyDescriptor {
	        public String getDisplayName() {
	            return "flyRoot";
	        }

	        public UserProperty newInstance(User user) {
	            return new FlyUser(false); 
	        }

	        @Override
	        public UserProperty newInstance(StaplerRequest req, JSONObject formData) throws FormException {
	            return new FlyUser(formData.optBoolean("flyRoot"));
	        }
	    }
}
