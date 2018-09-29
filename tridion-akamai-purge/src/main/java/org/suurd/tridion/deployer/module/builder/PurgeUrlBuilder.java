package org.suurd.tridion.deployer.module.builder;

import java.util.List;

import org.suurd.tridion.content.client.model.WebResourceItem;

public interface PurgeUrlBuilder {

	List<String> buildUrlsToPurge(int publicationId, List<WebResourceItem> publishedItems);

}
