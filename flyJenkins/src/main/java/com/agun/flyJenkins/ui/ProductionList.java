package com.agun.flyJenkins.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.agun.flyJenkins.model.ProductionMeta;
import com.agun.flyJenkins.persistence.ProductionSaveable;

import hudson.Extension;

@Extension
public class ProductionList extends FlyUI {

	@Override
	public String getDescription() {
		return "production list";
	}

	public List<ProductionMeta> getProductionMetaList(String jobName){
		ProductionSaveable productionSaveable = new ProductionSaveable();
		productionSaveable.load();
		List<ProductionMeta> productionMetaList = productionSaveable.getProductionMetaList();
		if(jobName == null)
			return productionMetaList;
		if(productionMetaList == null)
			return Collections.EMPTY_LIST;
		
		List<ProductionMeta> saveProductionMetaList = new ArrayList<ProductionMeta>();
		
		for(ProductionMeta productionMeta : productionMetaList){
			if(productionMeta.getJobName().equals(jobName)){
				saveProductionMetaList.add(productionMeta);
			}
		}
		return saveProductionMetaList;
	}
	
	@Extension
	public static class DescriptorImpl extends FlyUIDescriptor {
		
	}
}
