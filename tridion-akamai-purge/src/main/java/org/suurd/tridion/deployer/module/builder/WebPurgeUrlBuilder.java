package org.suurd.tridion.deployer.module.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suurd.tridion.content.client.model.WebResource;
import org.suurd.tridion.deployer.module.configuration.WebPurgeTypeConfiguration;
import org.suurd.tridion.discovery.client.DiscoveryServiceClient;

/**
 * Implementation of the <code>PurgeUrlBuilder</code> interface that builds a
 * list of URL's to purge formatted for a web application.
 * 
 * @author jsuurd
 */
public class WebPurgeUrlBuilder implements PurgeUrlBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(WebPurgeUrlBuilder.class);

	private WebPurgeTypeConfiguration configuration;

	private DiscoveryServiceClient discoveryClientFacade;

	/**
	 * Constructs a web purge URL builder with the specified configuration and
	 * discovery client facade.
	 * 
	 * @param configuration the configuration
	 * @param discoveryClientFacade the discovery client facade
	 */
	public WebPurgeUrlBuilder(WebPurgeTypeConfiguration configuration, DiscoveryServiceClient discoveryClientFacade) {
		super();

		this.configuration = configuration;
		this.discoveryClientFacade = discoveryClientFacade;
	}

	@Override
	public List<String> buildUrlsToPurge(int publicationId, List<WebResource> publishedItems) {
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

	protected List<String> buildUrlsToPurge(List<String> baseUrls, List<WebResource> publishedItems) {
		List<String> urls = new ArrayList<>();

		String[] defaultPageExtensions = configuration.getDefaultPageExtensions();
		for (WebResource publishedItem : publishedItems) {
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
