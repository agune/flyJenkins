package com.agun.flyJenkins.ui;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import hudson.Extension;

import com.agun.flyJenkins.service.InstanceModel;
import com.agun.flyJenkins.service.NetworkSpace;
import com.agun.flyJenkins.ui.ConfigServiceMeta.DescriptorImpl;
import com.agun.flyJenkins.user.FlyUser;
import com.agun.flyJenkins.util.AjaxProxy;
@Extension
public class ProcessInfo extends FlyUI {

	@Override
	public String getDescription() {
		return "You can see process Info of Service";
	}
	
	
	public List<InstanceModel> getInstanceModel(String host){
		
		System.out.println("get instance model ====> " + host);
		NetworkSpace networkSpace =NetworkSpace.getInstance();
		Map<String, List<InstanceModel>> instanceMap = networkSpace.getInstanceModelMap();
		if(instanceMap == null || instanceMap.containsKey(host) == false){
			System.out.println("====> empty");
			return Collections.EMPTY_LIST;
		}
		return instanceMap.get(host);
	}
	
	public boolean isFlyRoot(){
		  return  FlyUser.isFlyRoot();
	}
	
	@Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }
 
 	public AjaxProxy getAjaxProxy(){
 		return AjaxProxy.getInstance();
 	}

    @Extension
    public static class DescriptorImpl extends FlyUIDescriptor {
    	
    	@Override
        public String getDisplayName() {
            return "";
        }
    }

}
