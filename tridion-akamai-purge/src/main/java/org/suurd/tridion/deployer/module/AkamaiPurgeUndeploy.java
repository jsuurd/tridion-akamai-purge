package org.suurd.tridion.deployer.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suurd.tridion.content.client.ContentServiceClient;
import org.suurd.tridion.content.client.ODataV2ContentServiceClient;
import org.suurd.tridion.content.client.model.BinaryVariant;
import org.suurd.tridion.content.client.model.Page;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.deployer.Processor;
import com.tridion.transport.transportpackage.PageKey;
import com.tridion.transport.transportpackage.TransportPackage;

/**
 * SDL Deployer module which will send a purge request to the Akamai Content
 * Control Utility API when un-deploying content.
 * 
 * @author jsuurd
 */
public class AkamaiPurgeUndeploy extends AbstractAkamaiPurgeModule {

	private static final Logger LOG = LoggerFactory.getLogger(AkamaiPurgeUndeploy.class);

	private ContentServiceClient contentServiceClient;

	/**
	 * Constructs an Akamai purge un-deploy module with the specified configuration
	 * and processor.
	 * 
	 * @param config the configuration
	 * @param processor the processor
	 * @throws ConfigurationException if an error occurs processing the configuration
	 */
	public AkamaiPurgeUndeploy(Configuration config, Processor processor) throws ConfigurationException {
		super(config, processor);
		
		contentServiceClient = new ODataV2ContentServiceClient();
	}

	@Override
	protected List<BinaryVariant> getPublishedBinaries(TransportPackage transportPackage) {
		// Binaries are not part of the transport package when unpublishing 
		return Collections.emptyList();
	}

	@Override
	protected List<Page> getPublishedPages(TransportPackage transportPackage) {
		List<Page> pages = new ArrayList<>();
		
		Iterator<Object> arguments = transportPackage.getProcessorInstructions().getArguments();
		while (arguments.hasNext()) {
			Object argument = arguments.next();
			if (argument instanceof PageKey) {
				PageKey pageKey = (PageKey) argument;
				try {
					Page page = contentServiceClient.getPage(pageKey.getId());
					if (page != null) {
						pages.add(page);
					}
				} catch (Exception e) {
					// Only log and skip page
					LOG.error("Retrieving page failed [tcmuri={}], page will not be purged; cause [{}]", pageKey.getId(), e.toString());
				}
			}
		}
		
		return pages;
	}

	@Override
	protected Logger log() {
		return LOG;
	}

}
