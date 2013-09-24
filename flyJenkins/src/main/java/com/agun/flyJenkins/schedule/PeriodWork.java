package com.agun.flyJenkins.schedule;

import java.util.List;
import java.util.Map;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import com.agun.flyJenkins.model.DeployLog;
import com.agun.flyJenkins.model.DeployReport;
import com.agun.flyJenkins.deploy.DeploySurveillant;
import com.agun.flyJenkins.request.RequestQueue;

import hudson.Extension;
import hudson.model.PeriodicWork;


/**
 * deploy queue 를 주기적으로 체크 하여 deploy 진행을 함
 * @author agun
 *
 */

@Extension
public class PeriodWork extends PeriodicWork {
	
	private RequestQueue requestQueue = new RequestQueue();
	private DeploySurveillant deploySurveillant = new DeploySurveillant();
	
	public RequestQueue getRequestQueue(){
		return requestQueue;
	}

	public DeploySurveillant getDeploySurveillant(){
		return deploySurveillant;
	}
	
	public Map<String, DeployReport> getDeployReportMap(){
		return deploySurveillant.getDeployReportMap();
	}
	
	@Override
	public long getRecurrencePeriod() {
		//return MIN;
		return 10000;
	}

	@IgnoreJRERequirement
	@Override
	protected void doRun() throws Exception {
		deploySurveillant.process();
	}
}
