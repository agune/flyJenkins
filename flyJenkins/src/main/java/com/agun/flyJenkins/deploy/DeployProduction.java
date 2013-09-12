package com.agun.flyJenkins.deploy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jenkins.model.Jenkins;

import hudson.FilePath;
import hudson.model.AbstractBuild;

import com.agun.flyJenkins.model.ProductionMeta;
import com.agun.flyJenkins.persistence.ProductionSaveable;
import com.agun.flyJenkins.util.FlyJenkinsEnv;

/**
 * this class handle production when build 
 * @author agun
 *
 */
public class DeployProduction {

	public final static String SUCCESS = "SUCCESS";
	public final static String FAIL = "FAILURE";

	/**
	 * check state of building
	 * @param result
	 * @return
	 */
	public static boolean isSuccess(String result){
		return (result == null || "SUCCESS".equals(result) == false) ? false : true;
	}

	/**
	 * handle production when build
	 * @param jobName
	 * @param production
	 * @param build
	 * @return
	 */
	public boolean process(String jobName, String production, int serviceGroup, AbstractBuild<?,?> build){
		ProductionMeta productionMeta = normalizationProductionMeta(jobName, production, serviceGroup, build);
		makeJobDir(productionMeta, build);
		compressToCopy(productionMeta);
		saveProductionMeta(productionMeta);
		return true;
	}
	
	
	private ProductionMeta normalizationProductionMeta(String jobName, String production, int serviceGroup, AbstractBuild<?,?> build){
		
		ProductionMeta productionMeta = new ProductionMeta();
		productionMeta.setJobName(jobName);
		productionMeta.setCreateDate(new Date());
		productionMeta.setBuildNumber(build.getNumber());
		productionMeta.setProductionPath(production);
		productionMeta.setServiceGroup(serviceGroup);
		
		FilePath workSpace = build.getWorkspace();
		String baseName = getBaseName(production);
		String productionPathOfJob = FlyJenkinsEnv.getProductionRoot() + "/"+ jobName + "/" + build.getNumber() + "/" + baseName + ".zip";
		productionMeta.setProductionPathOfJob(productionPathOfJob);
		
		
		return productionMeta;
	}
	
	
	
	private void makeJobDir(ProductionMeta productionMeta, AbstractBuild<?,?> build){
		String jobPathString = FlyJenkinsEnv.getProductionRoot() + "/" + productionMeta.getJobName() + "/" + productionMeta.getBuildNumber();
		System.out.println("========> job path : " + jobPathString);
		
		FilePath jobPath = new FilePath(new File(jobPathString));
		try {
			if(jobPath.exists() == false){
				jobPath.mkdirs();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void compressToCopy(ProductionMeta productionMeta){
		String targetPathString = productionMeta.getProductionPath();
		String destinationPathString =productionMeta.getProductionPathOfJob();
		
		System.out.println("copy to ====>" +  targetPathString + "," + destinationPathString);
	
		FilePath targetPath = new FilePath(new File(targetPathString));
		FilePath destinationPath = new FilePath(new File(destinationPathString));
		try {
			if(targetPath.exists() == false)
				return;
			System.out.println("=========> process copy "  );
			targetPath.zip(destinationPath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	private void saveProductionMeta(ProductionMeta productionMeta){
		ProductionSaveable productionSaveable = new ProductionSaveable();
		productionSaveable.load();
		
		List<ProductionMeta> productionMetaList = productionSaveable.getProductionMetaList();
		
		if(productionMetaList == null){
			productionMetaList = new ArrayList<ProductionMeta>();
		}
		
		productionMetaList.add(productionMeta);
		productionSaveable.setProductionMetaList(productionMetaList);
			
		try {
			productionSaveable.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private String getName(String filePathString) {
        String r = filePathString;
        if(r.endsWith("\\") || r.endsWith("/"))
            r = r.substring(0,r.length()-1);

        int len = r.length()-1;
        while(len>=0) {
            char ch = r.charAt(len);
            if(ch=='\\' || ch=='/')
                break;
            len--;
        }
        return r.substring(len+1);
    }
	
	private  String getBaseName(String filePathString) {
		String n = getName(filePathString);
		int idx = n.lastIndexOf('.');
		if (idx<0)  return n;
		return n.substring(0,idx);
	}
	
	
	
}
