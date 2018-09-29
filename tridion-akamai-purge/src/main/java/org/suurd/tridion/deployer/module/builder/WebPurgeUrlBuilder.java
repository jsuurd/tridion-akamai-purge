package org.suurd.tridion.deployer.module.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suurd.tridion.content.client.model.WebResourceItem;
import org.suurd.tridion.deployer.module.configuration.WebPurgeTypeConfiguration;
import org.suurd.tridion.discovery.client.DiscoveryServiceClient;

public class WebPurgeUrlBuilder implements PurgeUrlBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(WebPurgeUrlBuilder.class);

	private WebPurgeTypeConfiguration configuration;

	private DiscoveryServiceClient discoveryClientFacade;

	public WebPurgeUrlBuilder(WebPurgeTypeConfiguration configuration, DiscoveryServiceClient discoveryClientFacade) {
		super();
		
		this.configuration = configuration;
		this.discoveryClientFacade = discoveryClientFacade;
	}

	public List<String> buildUrlsToPurge(int publicationId, List<WebResourceItem> publishedItems) {
		List<String> baseUrls = discoveryClientFacade.getWebApplicationBaseUrls(publicationId);
		filterBaseUrlsToExclude(baseUrls);
		if (baseUrls.isEmpty()) {
			LOG.warn("No base URL mapping available for publication [publicationId={}]", publicationId);
			return Collections.emptyList();
		}
		
		return buildUrlsToPurge(baseUrls, publishedItems);
	}

	protected void filterBaseUrlsToExclude(List<String> baseUrls) {
		String excludeBaseUrlsRegex = configuration.getExcludeBaseUrlsRegex();
		if (excludeBaseUrlsRegex != null) {
			for (Iterator<String> iterator = baseUrls.iterator(); iterator.hasNext();) {
				if (iterator.next().matches(excludeBaseUrlsRegex)) {
					iterator.remove();
				}
			}
		}
	}

	protected List<String> buildUrlsToPurge(List<String> baseUrls, List<WebResourceItem> publishedItems) {
		List<String> urls = new ArrayList<>();
		
		String[] defaultPageExtensions = configuration.getDefaultPageExtensions();
		for (WebResourceItem publishedItem : publishedItems) {
			for (String baseUrl : baseUrls) {
				String url = baseUrl + publishedItem.getUrl();
				urls.add(url);
				if (defaultPageExtensions != null) {
					for (String pageExtension : defaultPageExtensions) {
						if (url.endsWith(pageExtension)) {
							urls.add(url.substring(0, url.length() - pageExtension.length()));
						}
					}
				}
			}
		}
		
		return urls;
	}
}
