package com.agun.flyJenkins.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Level;

import jenkins.model.Jenkins;

import net.sf.json.JSONObject;

import org.codehaus.groovy.runtime.metaclass.NewInstanceMetaMethod;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.agun.flyJenkins.HelloWorldBuilder.DescriptorImpl;
import com.agun.flyJenkins.service.ServerMeta;

import hudson.BulkChange;
import hudson.Extension;
import hudson.RelativePath;
import hudson.XmlFile;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Descriptor.FormException;
import hudson.model.listeners.SaveableListener;
import hudson.util.FormValidation;

@Extension
public class ConfigServiceMeta extends FlyUI {

	private List<ServerMetaDescribable> serverMetaList = new ArrayList<ServerMetaDescribable>();
	private ServerMeta serverMeta;
    public ConfigServiceMeta() {
    	serverMeta = new ServerMeta();
    	System.out.println("ConfigServiceMeta =====> : " +  serverMeta.getHost());
    }
    public void doSave(final StaplerRequest request, final 
    		StaplerResponse response) { 

    	String host  = request.getParameter("_.host");
    	String testCmd  = request.getParameter("_.testCmd");
    	String weight  = request.getParameter("_.weight");
        String destination =  request.getParameter("_.destination");
    
        
        serverMeta.setHost(host);
        serverMeta.setTestCmd(testCmd);
        serverMeta.setWeight(Integer.parseInt(weight));
        serverMeta.setDestination(destination);
        
        serverMeta.save();
        
        
    }
    
    

   
    
    @Override
    public String getDescription() {
        return "How to access values of the nearby input fields when you do form field validation";
    }

    public List<ServerMetaDescribable> getServerMetaList() {
        return serverMetaList;
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
        public int weight;
        
        @Extension
        public static class DescriptorImpl extends Descriptor<ServerMetaDescribable> {
            @Override
            public String getDisplayName() {
                return "";
            }

            public FormValidation doCheckHost(@QueryParameter String value,
                                                   @RelativePath("host") @QueryParameter String name) {

            	System.out.println("=====================> check host : " + name + " , value : " +  value );
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
    	
    	
    	public ServerMeta getServerMeta() {
			return serverMeta;
		}


		public void setServerMeta(ServerMeta serverMeta) {
			this.serverMeta = serverMeta;
		}
    }
}
