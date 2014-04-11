/**
 * This class is root of flyJenkins.
 * @author agun
 * 
 */
package org.flyJenkins.action;

import org.flyJenkins.boot.FlyBootstrap;

import hudson.Extension;
import hudson.model.RootAction;


@Extension
public class FlyRootAction implements RootAction {

	/**
	 * flyJenkins does booting.
	 */
	public FlyRootAction(){
	
		System.out.println("====> new flyRootAction");

		FlyBootstrap.start();
	}
	
	public String getDisplayName() {
		return "flyJenkins";
	}

	public String getIconFileName() {
		return "gear.png";
	}

	public String getUrlName() {
		return "flyJenkins";
	}

}
