package com.agun.flyJenkins.ui;

import hudson.model.Descriptor;

public class FlyUIDescriptor extends Descriptor<FlyUI> {

	@Override
	public String getDisplayName() {
		return clazz.getSimpleName();
	}
}
