package org.suurd.tridion.deployer.module.configuration;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;

public class WebPurgeTypeConfiguration {

	private String[] defaultPageExtensions;

	private String excludeBaseUrlsRegex;

	public WebPurgeTypeConfiguration() {
		super();
	}

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

	public String[] getDefaultPageExtensions() {
		return defaultPageExtensions;
	}

	public void setDefaultPageExtensions(String[] defaultPageExtensions) {
		this.defaultPageExtensions = defaultPageExtensions;
	}

	public String getExcludeBaseUrlsRegex() {
		return excludeBaseUrlsRegex;
	}

	public void setExcludeBaseUrlsRegex(String excludeBaseUrlsRegex) {
		this.excludeBaseUrlsRegex = excludeBaseUrlsRegex;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
