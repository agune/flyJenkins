package com.agun.flyJenkins.ui;

import java.util.ArrayList;
import java.util.List;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.agun.flyJenkins.service.ServerMeta;

import hudson.Extension;
import hudson.RelativePath;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;

@Extension
public class ConfigServiceMeta extends FlyUI {

	ServerMeta serverMetaContain = new ServerMeta();
	public ConfigServiceMeta() {
		serverMetaContain = new ServerMeta();
		serverMetaContain.load();
	
	}
    
    public void doSave(final StaplerRequest request, final 
    		StaplerResponse response) { 

    	String host  = request.getParameter("_.host");
    	String testCmd  = request.getParameter("_.testCmd");
        String destination =  request.getParameter("_.destination");
     	String testUrl  = request.getParameter("_.testUrl");
    	int serverId  = Integer.parseInt(request.getParameter("_.serverId"));
    	int groupId  = Integer.parseInt(request.getParameter("_.groupId"));
    	int type  = Integer.parseInt(request.getParameter("_.type"));
    	int weight = Integer.parseInt(request.getParameter("_.weight"));
    	
    	ServerMeta serverMeta = new ServerMeta();
        serverMeta.setHost(host);
        serverMeta.setTestUrl(testUrl);
        serverMeta.setTestCmd(testCmd);
        serverMeta.setWeight(weight);
        serverMeta.setDestination(destination);
        serverMeta.setServerId(serverId);
        serverMeta.setGroupId(groupId);
        serverMeta.setType(type);
        serverMeta.save();
        serverMetaContain.load();
    }
    
    public ServerMetaDescribable getServerMetaDescribable(){
    	return new ServerMetaDescribable();
    }

   
    
    @Override
    public String getDescription() {
        return "You can setting meta info of deploy service";
    }

    public List<ServerMetaDescribable> getServerMetaList() {
    	List<ServerMeta> serverMetaList = serverMetaContain.getServerMetaList();
    	List<ServerMetaDescribable> serverMetaDescribableList = new ArrayList<ConfigServiceMeta.ServerMetaDescribable>();
    	if(serverMetaList != null){
    		for(ServerMeta serverMeta : serverMetaList){
    			ServerMetaDescribable serverMetaDescribable = new ServerMetaDescribable();
    			serverMetaDescribable.destination = serverMeta.getDestination();
    			serverMetaDescribable.host = serverMeta.getHost();
    			serverMetaDescribable.serverId = serverMeta.getServerId();
    			serverMetaDescribable.groupId = serverMeta.getGroupId();
    			serverMetaDescribable.testCmd = serverMeta.getTestCmd();
    			serverMetaDescribable.testUrl = serverMeta.getTestUrl();
    			serverMetaDescribable.weight = serverMeta.getWeight();
    			serverMetaDescribable.type = serverMeta.getType();
    			serverMetaDescribableList.add(serverMetaDescribable);
    		}
    	}
    	return serverMetaDescribableList;
    }

    @Override
    public List<SourceFile> getSourceFiles() {
        List<SourceFile> r = super.getSourceFiles();
        r.add(new SourceFile("ServerMetaDescribable/config.groovy"));
        return r;
    }

    public static class ServerMetaDescribable extends AbstractDescribableImpl<ServerMetaDescribable> {
        /*
            I'm lazy and just exposing fields as opposed to getter/setter.
            Jenkins doesn't care and works correctly either way.
         */
        public String destination;
        public String host;
        public String testCmd;
        public String testUrl;
        public int weight;
        public int type;
        public int groupId;
        public int serverId;
        
        
        @Extension
        public static class DescriptorImpl extends Descriptor<ServerMetaDescribable> {
            @Override
            public String getDisplayName() {
                return "";
            }
           
            public FormValidation doCheckHost(@QueryParameter String value,
                                                   @RelativePath("host") @QueryParameter String name) {

                return FormValidation.ok("Are you sure " + name + " is a capital of " + value + "?");
            }
        }
        
      
        
        @Override
        public DescriptorImpl getDescriptor() {
            return (DescriptorImpl)super.getDescriptor();
        }
    }
    
    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl)super.getDescriptor();
    }

    @Extension
    public static class DescriptorImpl extends FlyUIDescriptor {
    	
    	private ServerMeta serverMeta;
    	
    	
    	  /**
         * This method determines the values of the album drop-down list box.
         */
        public ListBoxModel doFillTypeItems() {
            ListBoxModel m = new ListBoxModel();
            m.add("general process","1");
            m.add("tomcat","2");
            m.add("etc","3");
            return m;
        }
    	
    	public ServerMeta getServerMeta() {
			return serverMeta;
		}

		public void setServerMeta(ServerMeta serverMeta) {
			this.serverMeta = serverMeta;
		}
    }
}
