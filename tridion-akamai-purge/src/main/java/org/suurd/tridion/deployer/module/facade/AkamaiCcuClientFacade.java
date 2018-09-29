package org.suurd.tridion.deployer.module.facade;

import java.util.List;

import org.suurd.akamai.ccu.client.model.v2.Domain;

public interface AkamaiCcuClientFacade {

	String invalidateArls(List<String> arls, Domain domain, String referenceId);

	String invalidateArls(List<String> arls, Domain domain, boolean waitForCompletion, int maxWaitTime, String referenceId);

}
