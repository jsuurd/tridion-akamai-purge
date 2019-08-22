package org.suurd.tridion.deployer.module;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suurd.tridion.content.client.model.BinaryVariant;
import org.suurd.tridion.content.client.model.Page;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.deployer.Processor;
import com.tridion.transport.transportpackage.BinaryMetaData;
import com.tridion.transport.transportpackage.PageMetaData;
import com.tridion.transport.transportpackage.TransportPackage;

/**
 * SDL Deployer module which will send a purge request to the Akamai Content
 * Control Utility API when deploying content.
 * 
 * @author jsuurd
 */
@SuppressWarnings("deprecation")
public class AkamaiPurgeDeploy extends AbstractAkamaiPurgeModule {

	private static final Logger LOG = LoggerFactory.getLogger(AkamaiPurgeDeploy.class);

	private static final String METADATA_TYPE_PAGES = "Pages";

	private static final String METADATA_TYPE_BINARIES = "Binaries";

	/**
	 * Constructs an Akamai purge deploy module with the specified configuration and
	 * processor.
	 * 
	 * @param config the configuration
	 * @param processor the processor
	 * @throws ConfigurationException if an error occurs processing the configuration
	 */
	public AkamaiPurgeDeploy(Configuration config, Processor processor) throws ConfigurationException {
		super(config, processor);
	}

	@Override
	protected List<BinaryVariant> getPublishedBinaries(TransportPackage transportPackage) {
		List<BinaryVariant> publishedBinaries = new ArrayList<>();
		
		BinaryMetaData binaryMetaData = (BinaryMetaData) transportPackage.getMetaDataFile(METADATA_TYPE_BINARIES);
		if (binaryMetaData != null) {
			for (com.tridion.transport.transportpackage.Binary binary : binaryMetaData.getBinaries()) {
				BinaryVariant publishedBinary = new BinaryVariant();
				publishedBinary.setPublicationId(binary.getId().getPublicationId());
				publishedBinary.setBinaryId(binary.getId().getItemId());
				publishedBinary.setUrl(binary.getURLPath());
				publishedBinaries.add(publishedBinary);
			}
		}
		
		return publishedBinaries;
	}

	@Override
	protected List<Page> getPublishedPages(TransportPackage transportPackage) {
		List<Page> publishedPages = new ArrayList<>();
		
		PageMetaData pageMetadata = (PageMetaData) transportPackage.getMetaDataFile(METADATA_TYPE_PAGES);
		if (pageMetadata != null) {
			for (com.tridion.transport.transportpackage.Page page : pageMetadata.getPages()) {
				Page publishedPage = new Page();
				publishedPage.setPublicationId(page.getId().getPublicationId());
				publishedPage.setItemId(page.getId().getItemId());
				publishedPage.setUrl(page.getURLPath());
				publishedPages.add(publishedPage);
			}
		}
		
		return publishedPages;
	}

	@Override
	protected Logger log() {
		return LOG;
	};

}
