/**
 * flyJenkins 
 */
package com.agun.flyJenkins;

import java.util.List;

import com.agun.flyJenkins.process.FlyFactory;
import com.agun.flyJenkins.process.FlyProcess;
import com.agun.flyJenkins.ui.FlyUI;


public class FlyRootAction {

	public FlyRootAction(){
		System.out.println("====> old flyRootAction");
		FlyBootstrap.start();
	}
	
	
	public String getIconFileName() {
		return "gear.png";
	}

	public String getDisplayName() {
		return "flyJenkins";
	}

	public String getUrlName() {
		return "flyJenkins";
	}
	
	 public List<FlyUI> getAll() {
	        return FlyUI.all();
	  }
	
	 public FlyUI getDynamic(String name) {
	        for (FlyUI ui : getAll())
	            if (ui.getUrlName().equals(name))
	                return ui;
	        return null;
	    }
	
	 /**
	  * flyjenkins 의 process 를  구현
	  * @param processName
	  * @return
	  */
	 public FlyProcess getFlyProcess(String processName){
		 return FlyFactory.getProcess(processName);
	 }
	 

}
