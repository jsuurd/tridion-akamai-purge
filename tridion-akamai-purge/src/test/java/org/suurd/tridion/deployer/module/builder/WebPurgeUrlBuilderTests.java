package org.suurd.tridion.deployer.module.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.suurd.tridion.content.client.model.Page;
import org.suurd.tridion.content.client.model.WebResource;
import org.suurd.tridion.deployer.module.configuration.WebPurgeTypeConfiguration;
import org.suurd.tridion.discovery.client.ConfigurationDiscoveryServiceClient;
import org.suurd.tridion.discovery.client.DiscoveryServiceClient;

public class WebPurgeUrlBuilderTests {

	private DiscoveryServiceClient discoveryServiceClient;

	@Before
	public void setUp() {
		discoveryServiceClient = new ConfigurationDiscoveryServiceClient();
	}

	@Test
	public void buildArlsToPurge_WithValidDefaultPageExtensions_ShouldReturnDefaultPageUrlsAdded() {
		List<String> baseUrls = new ArrayList<>();
		baseUrls.add("http://baseurl.mock:80");
		
		List<WebResource> publishedItems = new ArrayList<>();
		Page page = new Page();
		page.setUrl("/path/page.html");
		publishedItems.add(page);
		page = new Page();
		page.setUrl("/path/index.html");
		publishedItems.add(page);
		
		String[] defaultPageExtensions = new String[] {".html", "index.html", "/index.html"};
		
		WebPurgeTypeConfiguration configuration = new WebPurgeTypeConfiguration();
		configuration.setDefaultPageExtensions(defaultPageExtensions);
		
		WebPurgeUrlBuilder purgeUrlBuilder = new WebPurgeUrlBuilder(configuration, discoveryServiceClient);
		List<String> arls = purgeUrlBuilder.buildUrlsToPurge(baseUrls, publishedItems);
		
		assertNotNull(arls);
		assertEquals(6, arls.size());
		assertTrue(arls.contains("http://baseurl.mock:80/path/page.html"));
		assertTrue(arls.contains("http://baseurl.mock:80/path/page"));
		assertTrue(arls.contains("http://baseurl.mock:80/path/index.html"));
		assertTrue(arls.contains("http://baseurl.mock:80/path/index"));
		assertTrue(arls.contains("http://baseurl.mock:80/path/"));
		assertTrue(arls.contains("http://baseurl.mock:80/path"));
	}

	@Test
	public void buildArlsToPurge_WithNullDefaultPageExtensions_ShouldReturnAbsolutePageUrls() {
		List<String> baseUrls = new ArrayList<>();
		baseUrls.add("http://baseurl.mock:80");
		
		List<WebResource> publishedItems = new ArrayList<>();
		Page page = new Page();
		page.setUrl("/path/page.html");
		publishedItems.add(page);
		page = new Page();
		page.setUrl("/path/index.html");
		publishedItems.add(page);
		
		String[] defaultPageExtensions = null;
		
		WebPurgeTypeConfiguration configuration = new WebPurgeTypeConfiguration();
		configuration.setDefaultPageExtensions(defaultPageExtensions);
		
		WebPurgeUrlBuilder purgeUrlBuilder = new WebPurgeUrlBuilder(configuration, discoveryServiceClient);
		List<String> arls = purgeUrlBuilder.buildUrlsToPurge(baseUrls, publishedItems);
		
		assertNotNull(arls);
		assertEquals(2, arls.size());
		assertTrue(arls.contains("http://baseurl.mock:80/path/page.html"));
		assertTrue(arls.contains("http://baseurl.mock:80/path/index.html"));
	}

	@Test
	public void filterBaseUrlsToExclude_WithValidRegex_ShouldReturnBaseUrlsRemovedFromList() {
		List<String> baseUrls = new ArrayList<>();
		baseUrls.add("http://baseurl.mock:80/path/page.html");
		baseUrls.add("http://baseurl-origin.mock:80/path/page.html");
		
		String excludeBaseUrlsRegex = ".*-origin\\..*";
		
		WebPurgeTypeConfiguration configuration = new WebPurgeTypeConfiguration();
		configuration.setExcludeBaseUrlsRegex(excludeBaseUrlsRegex);
		
		WebPurgeUrlBuilder purgeUrlBuilder = new WebPurgeUrlBuilder(configuration, discoveryServiceClient);
		purgeUrlBuilder.filterBaseUrlsToExclude(baseUrls);
		
		assertEquals(1, baseUrls.size());
		assertTrue(!baseUrls.contains("http://baseurl-origin.mock:80/path/page.html"));
		assertTrue(baseUrls.contains("http://baseurl.mock:80/path/page.html"));
	}

	@Test
	public void filterBaseUrlsToExclude_WithNullRegex_ShouldReturnNoBaseUrlsRemovedFromList() {
		List<String> baseUrls = new ArrayList<>();
		baseUrls.add("http://baseurl.mock:80/path/page.html");
		baseUrls.add("http://baseurl-origin.mock:80/path/page.html");
		
		String excludeBaseUrlsRegex = null;
		
		WebPurgeTypeConfiguration configuration = new WebPurgeTypeConfiguration();
		configuration.setExcludeBaseUrlsRegex(excludeBaseUrlsRegex);
		
		WebPurgeUrlBuilder purgeUrlBuilder = new WebPurgeUrlBuilder(configuration, discoveryServiceClient);
		purgeUrlBuilder.filterBaseUrlsToExclude(baseUrls);
		
		assertEquals(2, baseUrls.size());
	}

}
