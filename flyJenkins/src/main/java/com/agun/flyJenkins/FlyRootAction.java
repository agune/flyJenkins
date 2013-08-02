/**
 * flyJenkins 
 */
package com.agun.flyJenkins;

import hudson.Extension;
import hudson.model.RootAction;

@Extension
public class FlyRootAction implements RootAction {

	public String getIconFileName() {
		return "gear.png";
	}

	public String getDisplayName() {
		return "flyJenkins";
	}

	public String getUrlName() {
		return "flyJenkins";
	}
}
