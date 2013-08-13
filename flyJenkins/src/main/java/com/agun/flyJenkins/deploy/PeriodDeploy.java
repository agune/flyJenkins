package com.agun.flyJenkins.deploy;

import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

import hudson.Extension;
import hudson.model.PeriodicWork;


/**
 * deploy queue 를 주기적으로 체크 하여 deploy 진행을 함
 * @author agun
 *
 */

@Extension
public class PeriodDeploy extends PeriodicWork {

	@Override
	public long getRecurrencePeriod() {
		return MIN;
	}

	@IgnoreJRERequirement
	@Override
	protected void doRun() throws Exception {
	}
}
