package com.agun.flyJenkins.model.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.agun.flyJenkins.model.DeployLog;
import com.agun.flyJenkins.model.DeployReport;
import com.agun.flyJenkins.model.DeployRequest;
import com.agun.flyJenkins.model.ProductionMeta;
import com.agun.flyJenkins.model.ServiceMeta;

public class ModelSoting {
	public static void serviceSortByWeight(List<ServiceMeta> serviceMetaList){

		Comparator<ServiceMeta> comparator =  new Comparator<ServiceMeta>() {
			public int compare(ServiceMeta o1, ServiceMeta o2) {
				return (o1.getWeight() > o2.getWeight())? 1 : -1 ;
			}
		
		};
		Collections.sort(serviceMetaList, comparator);
	}
	
	public static void productionSortDate(List<ProductionMeta> productionList){
		Comparator<ProductionMeta> comparator =  new Comparator<ProductionMeta>() {
			public int compare(ProductionMeta o1, ProductionMeta o2) {
				return (o1.getCreateDate().getTime() < o2.getCreateDate().getTime())? 1 : -1 ;
			}
		};
		
		Collections.sort(productionList, comparator);

	}
	
	
	public static void deployRequestSortDate(List<DeployRequest> deployRequestList){
		Comparator<DeployRequest> comparator =  new Comparator<DeployRequest>() {
			public int compare(DeployRequest o1, DeployRequest o2) {
				return (o1.getDate().getTime() < o2.getDate().getTime())? 1 : -1 ;
			}
		};
		Collections.sort(deployRequestList, comparator);
	}
	
	public static void deployLogSortDate(List<DeployLog> deployLogList){
		Comparator<DeployLog> comparator =  new Comparator<DeployLog>() {
			public int compare(DeployLog o1, DeployLog o2) {
				return (o1.getDate().getTime() < o2.getDate().getTime())? 1 : -1 ;
			}
		};
		Collections.sort(deployLogList, comparator);
	}
	
	public static void deployReportSortId(List<DeployReport> deployReportList){
		Comparator<DeployReport> comparator =  new Comparator<DeployReport>() {
			public int compare(DeployReport o1, DeployReport o2) {
				return o2.getDeployId().compareTo(o1.getDeployId());
			}
		};
		Collections.sort(deployReportList, comparator);
	}
}
