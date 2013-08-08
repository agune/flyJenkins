package com.agun.flyJenkins.ui;

import hudson.Extension;

import com.agun.flyJenkins.ui.ConfigServiceMeta.DescriptorImpl;
@Extension
public class ProcessInfo extends FlyUI {

	@Override
	public String getDescription() {
		return "You can see process Info of Service";
	}
	
	
 @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends FlyUIDescriptor {
    	
    	@Override
        public String getDisplayName() {
            return "";
        }
    }

}
