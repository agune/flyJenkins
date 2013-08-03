/**
 * flyJenkins 
 */
package com.agun.flyJenkins;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import hudson.Extension;
import hudson.model.RootAction;
import hudson.util.ProcessTree.OSProcess;

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
	
	/**
	 * issue #9 를 위한 임시 저장 모듈 
	 * @param processList
	 */
	public void saveProcessList(Hashtable<Integer, String> processInfoMap){
		System.out.println("==============> start test : ");
		try{
			for(Entry<Integer, String> entry : processInfoMap.entrySet()){
				
				System.out.println("process List =====> " + entry.getKey()  + ", name : " + entry.getValue());
			}
		}catch(Exception e){
			e.printStackTrace();
			
			System.out.println(e.getMessage());
		}
	}
}
