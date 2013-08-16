package com.agun.flyJenkins.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.agun.flyJenkins.service.AgentService;
import com.agun.flyJenkins.service.NetworkSpace;
import com.agun.flyJenkins.service.ServerMeta;
import com.agun.flyJenkins.service.ServiceGroup;
import com.agun.flyJenkins.user.FlyUser;

import hudson.Extension;
import hudson.RelativePath;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;

@Extension
public class ConfigServiceMeta extends FlyUI {

	public ConfigServiceMeta() {
	
	}
	
    /**
     * 서비스 설정 데이터를 저장한다.
     * @param request
     * @param response
     */
    public void doSave(final StaplerRequest request, final 
    		StaplerResponse response) { 

    	/**
    	 * 권한 체크
    	 */
    	if(FlyUser.isFlyRoot() == false)
    		return;
    	
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
        
        /**
         * 네트워크 space 에 추가 한다.
         */
        NetworkSpace networkSpace = NetworkSpace.getInstance();
        networkSpace.attachServerMeta(serverMeta.getCopy());
        
        serverMeta.save();
        
        try {
			response.sendRedirect("/jenkins/flyJenkins/ConfigServiceMeta");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public ServerMetaDescribable getServerMetaDescribable(){
    	return new ServerMetaDescribable();
    }

   public boolean isFlyRoot(){
	  return  FlyUser.isFlyRoot();
   }
    
    @Override
    public String getDescription() {
        return "You can setting meta info of deploy service";
    }

    public List<ServerMetaDescribable> getServerMetaList() {
    	List<ServerMetaDescribable> serverMetaDescribableList = new ArrayList<ConfigServiceMeta.ServerMetaDescribable>();
    	NetworkSpace networkSpace = NetworkSpace.getInstance();
    	Map<String, List<AgentService>> networkMap = networkSpace.getNetworkMap();
    	
    	for(List<AgentService> agentServiceList : networkMap.values()){
    		for(AgentService agentService : agentServiceList){
    			ServiceGroup serviceGroup = agentService.getServiceGroup();
    		
    	    	List<ServerMeta> serverMetaList = serviceGroup.getServerMetaList();
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
    	    			serverMetaDescribable.pid = serverMeta.getPid();
    	    			serverMetaDescribableList.add(serverMetaDescribable);
    	    		}
    	    	}
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
        public int pid;
        
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
