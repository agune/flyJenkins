package com.agun.flyJenkins.ui;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import hudson.Extension;

import com.agun.flyJenkins.FlyFactory;
import com.agun.flyJenkins.user.FlyUser;
import com.agun.flyJenkins.util.AjaxProxy;
import com.agun.flyJenkins.network.NetworkSpace;
import com.agun.flyJenkins.model.InstanceModel;

@Extension
public class ProcessInfo extends FlyUI {

	@Override
	public String getDescription() {
		return "You can see process Info of Service";
	}
	
	
	public List<InstanceModel> getInstanceModel(String host){
		
		System.out.println("get instance model ====> " + host);
		NetworkSpace networkSpace = FlyFactory.getNetworkSpace();
		Map<String, List<InstanceModel>> instanceMap = networkSpace.getInstanceModelMap();
		System.out.println("====>  value = " + instanceMap);
		
		if(instanceMap == null || instanceMap.containsKey(host) == false){
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
