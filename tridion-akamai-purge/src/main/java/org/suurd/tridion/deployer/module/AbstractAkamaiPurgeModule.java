package org.suurd.tridion.deployer.module;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.suurd.akamai.ccu.client.model.v2.Domain;
import org.suurd.tridion.content.client.model.BinaryVariant;
import org.suurd.tridion.content.client.model.Page;
import org.suurd.tridion.content.client.model.WebResource;
import org.suurd.tridion.deployer.module.builder.PurgeUrlBuilder;
import org.suurd.tridion.deployer.module.builder.WebPurgeUrlBuilder;
import org.suurd.tridion.deployer.module.configuration.AkamaiConfiguration;
import org.suurd.tridion.deployer.module.configuration.ModuleConfiguration;
import org.suurd.tridion.deployer.module.configuration.WebPurgeTypeConfiguration;
import org.suurd.tridion.deployer.module.facade.AkamaiCcuClientFacade;
import org.suurd.tridion.deployer.module.facade.AkamaiCcuV2ClientFacade;
import org.suurd.tridion.deployer.module.model.WebApplicationExtensionProperties;
import org.suurd.tridion.deployer.module.model.WebApplicationExtensionProperties.CdnPurgeItems;
import org.suurd.tridion.discovery.client.ConfigurationDiscoveryServiceClient;
import org.suurd.tridion.discovery.client.DiscoveryServiceClient;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.deployer.Module;
import com.tridion.deployer.ProcessingException;
import com.tridion.deployer.Processor;
import com.tridion.transport.transportpackage.TransportPackage;
import com.tridion.util.TCMURI;

/**
 * Base class for SDL Deployer modules that will send purge requests to the
 * Akamai Content Control Utility API.
 * 
 * @author jsuurd
 */
public abstract class AbstractAkamaiPurgeModule extends Module {

	private ModuleConfiguration configuration;

	private DiscoveryServiceClient discoveryServiceClient;

	private PurgeUrlBuilder purgeUrlBuilder;

	private AkamaiCcuClientFacade ccuClientFacade;

	private Domain ccuDomain;

	private boolean isEnabled;

	/**
	 * Constructs an Akamai purge module with the specified configuration and processor.
	 * 
	 * @param config the configuration
	 * @param processor the processor
	 * @throws ConfigurationException if an error occurs processing the configuration
	 */
	public AbstractAkamaiPurgeModule(Configuration config, Processor processor) throws ConfigurationException {
		super(config, processor);
		
		if (log().isDebugEnabled()) {
			log().debug("Initializing Akamai Purge Module: {}", config);
		}
		
		try {
			this.configuration = new ModuleConfiguration(config);
			this.discoveryServiceClient = new ConfigurationDiscoveryServiceClient();
			this.purgeUrlBuilder = new WebPurgeUrlBuilder(new WebPurgeTypeConfiguration(config), this.discoveryServiceClient);
			
			AkamaiConfiguration akamaiConfiguration = new AkamaiConfiguration(config);
			this.ccuClientFacade = new AkamaiCcuV2ClientFacade(akamaiConfiguration);
			this.ccuDomain = akamaiConfiguration.getDomain();
			
			this.isEnabled = true;
			
		} catch (ConfigurationException e) {
			log().error("Error initializing Akamai Purge Module, module not enabled", e);
		}
	}

	@Override
	public void process(TransportPackage transportPackage) throws ProcessingException {
		if (!isEnabled) {
			log().warn("Akamai Purge Module is not enabled");
			return;
		}
		
		TCMURI transactionId = transportPackage.getTransactionId();
		
		if (log().isInfoEnabled()) {
			log().info("Purging content for publish transaction [transactionId={}]", transactionId);
		}
		
		try {
			int publicationId = transportPackage.getProcessorInstructions().getPublicationId().getItemId();
			WebApplicationExtensionProperties extensionProperties = new WebApplicationExtensionProperties(discoveryServiceClient.getWebApplicationExtensionProperties(publicationId));
			
			if (extensionProperties.isCdnPurgeEnabled())
			{
				CdnPurgeItems cdnPurgeItems = extensionProperties.getCdnPurgeItems();
				
				if (log().isDebugEnabled()) {
					log().debug("Purge settings [cdnPurgeItems={}]", cdnPurgeItems);
				}
				
				List<WebResource> publishedItems = new ArrayList<>();
				if (cdnPurgeItems == CdnPurgeItems.All || cdnPurgeItems == CdnPurgeItems.Binaries) {
					publishedItems.addAll(getPublishedBinaries(transportPackage));
				}
				if (cdnPurgeItems == CdnPurgeItems.All || cdnPurgeItems == CdnPurgeItems.Pages) {
					publishedItems.addAll(getPublishedPages(transportPackage));
				}
				
				List<String> urlsToPurge = purgeUrlBuilder.buildUrlsToPurge(publicationId, publishedItems);
				if (urlsToPurge.size() > 0) {
					ccuClientFacade.invalidateArls(urlsToPurge, ccuDomain, configuration.isWaitForCompletion(), configuration.getMaxWaitTime(), transactionId.toString());
				} else {
					if (log().isDebugEnabled()) {
						log().debug("No items to purge publish transaction [transactionId={}]", transactionId);
					}
				}
				
				if (log().isInfoEnabled()) {
					log().info("Successfully processed purge request for publish transaction [transactionId={}]", transactionId);
				}
			} else {
				if (log().isInfoEnabled()) {
					log().info("Purging content is disabled for publication [publicationId={}]. Content will not be purged for publish transaction [transactionId={}]", publicationId, transactionId);
				}
			}
			
		} catch (Exception e) {
			String message = MessageFormat.format("Error processing purge request for publish transaction [transactionId={0}]", transactionId);
			log().error(message, e);
			if (!configuration.isContinueOnError()) {
				if (e instanceof ProcessingException) {
					throw (ProcessingException)e;
				}
				throw new ProcessingException(message, e);
			}
		}
	}

	/**
	 * Gets the list of published binaries for the specified transport package.
	 * 
	 * @param transportPackage the transport package
	 * @return the list of published binaries
	 */
	protected abstract List<BinaryVariant> getPublishedBinaries(TransportPackage transportPackage);

	/**
	 * Gets the list of published pages for the specified transport package.
	 * 
	 * @param transportPackage the transport package
	 * @return the list of published pages
	 */
	protected abstract List<Page> getPublishedPages(TransportPackage transportPackage);

	/**
	 * Returns the logger.
	 * 
	 * @return the logger
	 */
	protected abstract Logger log();

}