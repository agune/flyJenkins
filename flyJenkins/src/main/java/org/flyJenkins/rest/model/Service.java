package org.flyJenkins.rest.model;

import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import hudson.Plugin;
import hudson.model.Api;

@ExportedBean
public class Service extends Plugin {

	public Api getApi(){
		return new Api(this);
	}
	
	@Exported
	public int getServiceId(){
		return 2;
	}
}
