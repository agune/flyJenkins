package com.agun.flyJenkins.job;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import hudson.Extension;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.ListViewColumn;

public class RequestButtonColumn extends ListViewColumn {
	public RequestButtonColumn(){
	}

	@Extension
    public static class DescriptorImpl extends ListViewColumnDescriptor {
		
		@Override
        public String getDisplayName() {
            return "request";
        }
    }
}
