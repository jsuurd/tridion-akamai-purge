package org.suurd.tridion.deployer.module.configuration;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;

/**
 * Class containing configuration properties used by the Akamai purge module.
 * 
 * @author jsuurd
 */
public class ModuleConfiguration {

	private static final int DEFAULT_WAIT_TIME = 1800;

	private boolean waitForCompletion;

	private int maxWaitTime;

	private boolean continueOnError;

	/**
	 * Constructs a module configuration.
	 */
	public ModuleConfiguration() {
		super();
	}

	/**
	 * Constructs a module configuration with the specified configuration.
	 *  
	 * @param config the configuration
	 * @throws ConfigurationException if an error occurs processing the configuration
	 */
	public ModuleConfiguration(Configuration config) throws ConfigurationException {
		super();
		
		Configuration waitForCompletionConfig = config.getChild("WaitForCompletion");
		this.waitForCompletion = waitForCompletionConfig.getBooleanValue("Enabled", false);
		if (this.waitForCompletion) {
			this.maxWaitTime = waitForCompletionConfig.getAttributeAsInt("MaxWaitTime", DEFAULT_WAIT_TIME);
		}
		
		this.continueOnError = config.getChild("ContinueOnError").getBooleanValue("Enabled", false);
	}

	/**
	 * Gets the if the module should wait for completion for the purge request.
	 * 
	 * @return the wait for completion
	 */
	public boolean isWaitForCompletion() {
		return waitForCompletion;
	}

	/**
	 * Sets the if the module should wait for completion of the purge request.
	 * 
	 * @param waitForCompletion the wait for for completion to set
	 */
	public void setWaitForCompletion(boolean waitForCompletion) {
		this.waitForCompletion = waitForCompletion;
	}

	/**
	 * Gets the maximum wait time wait for completion of the purge request.
	 * 
	 * @return the maximum wait time
	 */
	public int getMaxWaitTime() {
		return maxWaitTime;
	}

	/**
	 * Sets the maximum wait time wait for completion of the purge request.
	 * 
	 * @param maxWaitTime the maximum wait time to set
	 */
	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	/**
	 * Gets if the module should continue if an error occurs.
	 * 
	 * @return the continue on error
	 */
	public boolean isContinueOnError() {
		return continueOnError;
	}

	/**
	 * Sets if the module should continue if an error occurs.
	 * 
	 * @param continueOnError the continue on error to set
	 */
	public void setContinueOnError(boolean continueOnError) {
		this.continueOnError = continueOnError;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
