package com.agun.flyJenkins.ui;

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

	public List<ProductionMeta> getProductionMetaList(){
		ProductionSaveable productionSaveable = new ProductionSaveable();
		productionSaveable.load();
		return productionSaveable.getProductionMetaList();
	}
	
	@Extension
	public static class DescriptorImpl extends FlyUIDescriptor {
		
	}
}
