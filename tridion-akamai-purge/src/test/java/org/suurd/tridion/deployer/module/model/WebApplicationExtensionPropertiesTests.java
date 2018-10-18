package org.suurd.tridion.deployer.module.model;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.suurd.tridion.deployer.module.model.WebApplicationExtensionProperties;
import org.suurd.tridion.deployer.module.model.WebApplicationExtensionProperties.CdnPurgeItems;

public class WebApplicationExtensionPropertiesTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void isCdnPurgeEnabled_WithPropertyTrue_ShouldReturnTrue() {
		Map<String, String> extensionProperties = new HashMap<>();
		extensionProperties.put("CdnPurgeEnabled", "True");
		
		WebApplicationExtensionProperties webApplicationExtensionProperties = new WebApplicationExtensionProperties(extensionProperties);
		boolean isCdnPurgeEnabled = webApplicationExtensionProperties.isCdnPurgeEnabled();
		
		assertEquals(true, isCdnPurgeEnabled);
	}

	@Test
	public void isCdnPurgeEnabled_WithPropertyFalse_ShouldReturnFalse() {
		Map<String, String> extensionProperties = new HashMap<>();
		extensionProperties.put("CdnPurgeEnabled", "False");
		
		WebApplicationExtensionProperties webApplicationExtensionProperties = new WebApplicationExtensionProperties(extensionProperties);
		boolean isCdnPurgeEnabled = webApplicationExtensionProperties.isCdnPurgeEnabled();
		
		assertEquals(false, isCdnPurgeEnabled);
	}

	@Test
	public void isCdnPurgeEnabled_WithoutProperty_ShouldReturnTrue() {
		Map<String, String> extensionProperties = new HashMap<>();
		
		WebApplicationExtensionProperties webApplicationExtensionProperties = new WebApplicationExtensionProperties(extensionProperties);
		boolean isCdnPurgeEnabled = webApplicationExtensionProperties.isCdnPurgeEnabled();
		
		assertEquals(true, isCdnPurgeEnabled);
	}

	@Test
	public void isCdnPurgeEnabled_WithPropertiesNull_ShouldReturnTrue() {
		Map<String, String> extensionProperties = null;
		
		WebApplicationExtensionProperties webApplicationExtensionProperties = new WebApplicationExtensionProperties(extensionProperties);
		boolean isCdnPurgeEnabled = webApplicationExtensionProperties.isCdnPurgeEnabled();
		
		assertEquals(true, isCdnPurgeEnabled);
	}

	@Test
	public void isCdnPurgeEnabled_WithInvalidProperty_ShouldReturnFalse() {
		Map<String, String> extensionProperties = new HashMap<>();
		extensionProperties.put("CdnPurgeEnabled", "NonBooleanValue");
		
		WebApplicationExtensionProperties webApplicationExtensionProperties = new WebApplicationExtensionProperties(extensionProperties);
		boolean isCdnPurgeEnabled = webApplicationExtensionProperties.isCdnPurgeEnabled();
		
		assertEquals(false, isCdnPurgeEnabled);
	}

	@Test
	public void getCdnPurgeItems_WithPropertyBinaries_ShouldReturnBinaries() {
		Map<String, String> extensionProperties = new HashMap<>();
		extensionProperties.put("CdnPurgeItems", "Binaries");
		
		WebApplicationExtensionProperties webApplicationExtensionProperties = new WebApplicationExtensionProperties(extensionProperties);
		CdnPurgeItems cdnPurgeItems = webApplicationExtensionProperties.getCdnPurgeItems();
		
		assertEquals(CdnPurgeItems.Binaries, cdnPurgeItems);
	}

	@Test
	public void getCdnPurgeItems_WithPropertyPages_ShouldReturnPages() {
		Map<String, String> extensionProperties = new HashMap<>();
		extensionProperties.put("CdnPurgeItems", "Pages");
		
		WebApplicationExtensionProperties webApplicationExtensionProperties = new WebApplicationExtensionProperties(extensionProperties);
		CdnPurgeItems cdnPurgeItems = webApplicationExtensionProperties.getCdnPurgeItems();
		
		assertEquals(CdnPurgeItems.Pages, cdnPurgeItems);
	}

	@Test
	public void getCdnPurgeItems_WithoutPropertyPages_ShouldReturnAll() {
		Map<String, String> extensionProperties = new HashMap<>();
		
		WebApplicationExtensionProperties webApplicationExtensionProperties = new WebApplicationExtensionProperties(extensionProperties);
		CdnPurgeItems cdnPurgeItems = webApplicationExtensionProperties.getCdnPurgeItems();
		
		assertEquals(CdnPurgeItems.All, cdnPurgeItems);
	}

	@Test
	public void getCdnPurgeItems_WithPropertiesNull_ShouldReturnAll() {
		Map<String, String> extensionProperties = null;
		
		WebApplicationExtensionProperties webApplicationExtensionProperties = new WebApplicationExtensionProperties(extensionProperties);
		CdnPurgeItems cdnPurgeItems = webApplicationExtensionProperties.getCdnPurgeItems();
		
		assertEquals(CdnPurgeItems.All, cdnPurgeItems);
	}

	@Test
	public void getCdnPurgeItems_WithInvalidProperty_ShouldThrowIllegalArgumentException() {
		Map<String, String> extensionProperties = new HashMap<>();
		extensionProperties.put("CdnPurgeItems", "NonEnumValue");
		
		WebApplicationExtensionProperties webApplicationExtensionProperties = new WebApplicationExtensionProperties(extensionProperties);
		
		thrown.expect(IllegalArgumentException.class);
		webApplicationExtensionProperties.getCdnPurgeItems();
	}
}
