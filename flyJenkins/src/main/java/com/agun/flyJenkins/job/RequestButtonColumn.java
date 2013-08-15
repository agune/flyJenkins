package com.agun.flyJenkins.job;

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
