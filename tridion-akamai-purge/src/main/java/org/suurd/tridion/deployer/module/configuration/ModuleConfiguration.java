package org.suurd.tridion.deployer.module.configuration;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;

public class ModuleConfiguration {

	private static final int DEFAULT_WAIT_TIME = 1800;

	private boolean waitForCompletion;

	private int maxWaitTime;

	private boolean continueOnError;

	public ModuleConfiguration() {
		super();
	}

	public ModuleConfiguration(Configuration config) throws ConfigurationException {
		super();
		
		Configuration waitForCompletionConfig = config.getChild("WaitForCompletion");
		this.waitForCompletion = waitForCompletionConfig.getBooleanValue("Enabled", false);
		if (this.waitForCompletion) {
			this.maxWaitTime = waitForCompletionConfig.getAttributeAsInt("MaxWaitTime", DEFAULT_WAIT_TIME);
		}
		
		this.continueOnError = config.getChild("ContinueOnError").getBooleanValue("Enabled", false);
	}

	public boolean isWaitForCompletion() {
		return waitForCompletion;
	}

	public void setWaitForCompletion(boolean waitForCompletion) {
		this.waitForCompletion = waitForCompletion;
	}

	public int getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public boolean isContinueOnError() {
		return continueOnError;
	}

	public void setContinueOnError(boolean continueOnError) {
		this.continueOnError = continueOnError;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
