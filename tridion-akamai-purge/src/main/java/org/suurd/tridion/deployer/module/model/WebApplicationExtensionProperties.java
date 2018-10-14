package org.suurd.tridion.deployer.module.model;

import java.util.Map;

/**
 * Model representing SDL Topology Manager web application extension properties.
 * 
 * @author jsuurd
 */
public class WebApplicationExtensionProperties {

	protected final static String CDN_PURGE_ENABLED_KEY = "CdnPurgeEnabled";

	protected final static String CDN_PURGE_ITEMS_KEY = "CdnPurgeItems";

	protected final static boolean DEFAULT_CDN_PURGE_ENABLED_VALUE = true;

	protected final static CdnPurgeItems DEFAULT_CDN_PURGE_ITEMS_VALUE = CdnPurgeItems.All;

	private Map<String, String> extensionProperties;

	/**
	 * Constructs a web application extension properties with the specified
	 * extension properties key value pairs.
	 * 
	 * @param extensionProperties the extension properties
	 */
	public WebApplicationExtensionProperties(Map<String, String> extensionProperties) {
		super();
		
		this.extensionProperties = extensionProperties;
	}

	/**
	 * Gets if the CDN purge is enabled.
	 * 
	 * @return if CDN purge is enabled
	 */
	public boolean isCdnPurgeEnabled() {
		boolean isCdnPurgeEnabled = DEFAULT_CDN_PURGE_ENABLED_VALUE;
		if (extensionProperties != null && extensionProperties.containsKey(CDN_PURGE_ENABLED_KEY)) {
			isCdnPurgeEnabled = Boolean.parseBoolean(extensionProperties.get(CDN_PURGE_ENABLED_KEY));
		}
		return isCdnPurgeEnabled;
	}

	/**
	 * Gets the CDN purge items.
	 * 
	 * @return the CDN purge items
	 */
	public CdnPurgeItems getCdnPurgeItems() {
		CdnPurgeItems cdnPurgeItems = DEFAULT_CDN_PURGE_ITEMS_VALUE;
		if (extensionProperties != null && extensionProperties.containsKey(CDN_PURGE_ITEMS_KEY)) {
			cdnPurgeItems = CdnPurgeItems.valueOf(extensionProperties.get(CDN_PURGE_ITEMS_KEY));
		}
		return cdnPurgeItems;
	}

	/**
	 * Enumeration of the CDN items to purge, either binaries, pages or all.
	 * @author jsuurd
	 */
	public enum CdnPurgeItems {
		All,
		Binaries,
		Pages
	}

}
