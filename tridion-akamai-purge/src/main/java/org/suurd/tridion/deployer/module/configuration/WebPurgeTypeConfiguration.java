package org.suurd.tridion.deployer.module.configuration;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;

/**
 * Class containing configuration properties used by the
 * {@link org.suurd.tridion.deployer.module.builder.WebPurgeUrlBuilder}.
 * 
 * @author jsuurd
 */
public class WebPurgeTypeConfiguration {

	private String[] defaultPageExtensions;

	private String excludeBaseUrlsRegex;

	/**
	 * Constructs a web purge configuration.
	 */
	public WebPurgeTypeConfiguration() {
		super();
	}

	/**
	 * Constructs a web purge configuration with the specified configuration.
	 * 
	 * @param config the configuration
	 * @throws ConfigurationException if an error occurs processing the
	 *                                configuration
	 */
	public WebPurgeTypeConfiguration(Configuration config) throws ConfigurationException {
		super();

		Configuration webPurgeTypeConfig = config.getChild("WebPurgeType");

		if (webPurgeTypeConfig.hasAttribute("DefaultPageExtensions")) {
			this.defaultPageExtensions = webPurgeTypeConfig.getAttribute("DefaultPageExtensions").split(",");
		}

		if (webPurgeTypeConfig.hasAttribute("ExcludeBaseUrlsRegex")) {
			this.excludeBaseUrlsRegex = webPurgeTypeConfig.getAttribute("ExcludeBaseUrlsRegex");
		}
	}

	/**
	 * Gets the default page extensions.
	 * 
	 * @return the defaultPageExtensions
	 */
	public String[] getDefaultPageExtensions() {
		return defaultPageExtensions;
	}

	/**
	 * Sets the default page extensions.
	 * 
	 * @param defaultPageExtensions the default page extensions to set
	 */
	public void setDefaultPageExtensions(String[] defaultPageExtensions) {
		this.defaultPageExtensions = defaultPageExtensions;
	}

	/**
	 * Gets the exclude base URLs regex.
	 * 
	 * @return the excludeBaseUrlsRegex
	 */
	public String getExcludeBaseUrlsRegex() {
		return excludeBaseUrlsRegex;
	}

	/**
	 * Sets the exclude base URLs regex.
	 * 
	 * @param excludeBaseUrlsRegex the exclude base URLs regex to set
	 */
	public void setExcludeBaseUrlsRegex(String excludeBaseUrlsRegex) {
		this.excludeBaseUrlsRegex = excludeBaseUrlsRegex;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
