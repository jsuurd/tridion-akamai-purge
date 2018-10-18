package org.suurd.tridion.deployer.module.builder;

import java.util.List;

import org.suurd.tridion.content.client.model.WebResourceItem;

/**
 * Builder responsible for building a list of URL's to purge.
 *  
 * @author jsuurd
 */
public interface PurgeUrlBuilder {

	/**
	 * Builds a list of URL's to purge for the specified publication ID and
	 * published items.
	 * 
	 * @param publicationId the publication ID
	 * @param publishedItems the published items
	 * @return the list of URL's
	 */
	List<String> buildUrlsToPurge(int publicationId, List<WebResourceItem> publishedItems);

}
