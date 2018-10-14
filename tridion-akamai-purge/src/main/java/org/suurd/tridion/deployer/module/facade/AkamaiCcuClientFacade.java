package org.suurd.tridion.deployer.module.facade;

import java.util.List;

import org.suurd.akamai.ccu.client.model.v2.Domain;

/**
 * Facade for the Akamai CCU client.
 * 
 * @author jsuurd
 */
public interface AkamaiCcuClientFacade {

	/**
	 * Submits a purge request to invalidate a list or ARLs without waiting for
	 * completion of the purge request. Returns the id of the purge request.
	 * 
	 * @param arls the ARLs
	 * @param domain the domain
	 * @param referenceId the reference id
	 * @return the purge request id
	 */
	String invalidateArls(List<String> arls, Domain domain, String referenceId);

	/**
	 * Submits a purge request to invalidate a list or ARLs optionally waiting for
	 * completion of the purge request. Returns the id of the purge request.
	 * 
	 * @param arls the ARLs
	 * @param domain the domain
	 * @param waitForCompletion the wait for completion
	 * @param maxWaitTime the maximum wait time
	 * @param referenceId the reference id
	 * @return the purge request id
	 */
	String invalidateArls(List<String> arls, Domain domain, boolean waitForCompletion, int maxWaitTime, String referenceId);

}
