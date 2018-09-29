package org.suurd.tridion.deployer.module.model;

import java.util.Map;

public class WebApplicationExtensionProperties {

	protected final static String CDN_PURGE_ENABLED_KEY = "CdnPurgeEnabled";

	protected final static String CDN_PURGE_ITEMS_KEY = "CdnPurgeItems";

	protected final static boolean DEFAULT_CDN_PURGE_ENABLED_VALUE = true;

	protected final static CdnPurgeItems DEFAULT_CDN_PURGE_ITEMS_VALUE = CdnPurgeItems.All;

	private Map<String, String> extensionProperties;

	public WebApplicationExtensionProperties(Map<String, String> extensionProperties) {
		super();
		
		this.extensionProperties = extensionProperties;
	}

	public boolean isCdnPurgeEnabled() {
		boolean isCdnPurgeEnabled = DEFAULT_CDN_PURGE_ENABLED_VALUE;
		if (extensionProperties != null && extensionProperties.containsKey(CDN_PURGE_ENABLED_KEY)) {
			isCdnPurgeEnabled = Boolean.parseBoolean(extensionProperties.get(CDN_PURGE_ENABLED_KEY));
		}
		return isCdnPurgeEnabled;
	}

	public CdnPurgeItems getCdnPurgeItems() {
		CdnPurgeItems cdnPurgeItems = DEFAULT_CDN_PURGE_ITEMS_VALUE;
		if (extensionProperties != null && extensionProperties.containsKey(CDN_PURGE_ITEMS_KEY)) {
			cdnPurgeItems = CdnPurgeItems.valueOf(extensionProperties.get(CDN_PURGE_ITEMS_KEY));
		}
		return cdnPurgeItems;
	}

	public enum CdnPurgeItems {
		All,
		Binaries,
		Pages
	}

}
