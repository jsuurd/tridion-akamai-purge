package org.suurd.tridion.deployer.module.facade;

import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.suurd.akamai.ccu.client.CcuClient;
import org.suurd.akamai.ccu.client.CcuV2Client;
import org.suurd.akamai.ccu.client.facade.GoogleHttpClientEdgeGridFacade;
import org.suurd.akamai.ccu.client.model.v2.Action;
import org.suurd.akamai.ccu.client.model.v2.Domain;
import org.suurd.akamai.ccu.client.model.v2.PurgeRequest;
import org.suurd.akamai.ccu.client.model.v2.PurgeResponse;
import org.suurd.akamai.ccu.client.model.v2.PurgeStatus;
import org.suurd.akamai.ccu.client.model.v2.PurgeStatusRequest;
import org.suurd.akamai.ccu.client.model.v2.PurgeStatusResponse;
import org.suurd.akamai.ccu.client.model.v2.Type;
import org.suurd.akamai.ccu.client.provider.ApacheHttpTransportProvider;
import org.suurd.akamai.ccu.client.provider.ConfigurationProvider;
import org.suurd.akamai.ccu.client.provider.HttpTransportProvider;

/**
 * Implementation of the <code>AkamaiCcuClientFacade</code> interface using the
 * CCU V2 client.
 * 
 * @author jsuurd
 */
public class AkamaiCcuV2ClientFacade implements AkamaiCcuClientFacade {

	private static final Logger LOG = LoggerFactory.getLogger(AkamaiCcuV2ClientFacade.class);

	private CcuClient ccuClient;

	/**
	 * Constructs an Akamai CCU V2 client facade with the specified configuration provider.
	 * 
	 * @param configurationProvider the configuration provider
	 */
	public AkamaiCcuV2ClientFacade(ConfigurationProvider configurationProvider) {
		this(configurationProvider, new ApacheHttpTransportProvider());
	}

	/**
	 * Constructs an Akamai CCU V2 client facade with the specified configuration provider
	 * and http transport provider.
	 * 
	 * @param configurationProvider the configuration provider
	 * @param httpTransportProvider the http transport provider
	 */
	AkamaiCcuV2ClientFacade(ConfigurationProvider configurationProvider, HttpTransportProvider httpTransportProvider) {
		super();
		
		this.ccuClient = new CcuV2Client(configurationProvider, new GoogleHttpClientEdgeGridFacade(configurationProvider, httpTransportProvider));
	}

	@Override
	public String invalidateArls(List<String> arls, Domain domain, String referenceId) {
		return invalidateArls(arls, domain, false, 0, referenceId);
	}

	@Override
	public String invalidateArls(List<String> arls, Domain domain, boolean waitForCompletion, int maxWaitTime, String referenceId) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Submitting purge request [referenceId={}]", referenceId);
		}
		
		PurgeRequest purgeRequest = PurgeRequest.builder()
				.domain(domain)
				.action(Action.INVALIDATE)
				.type(Type.ARL)
				.objects(arls)
				.build();
		
		PurgeResponse purgeResponse = ccuClient.addPurgeRequest(purgeRequest);
		if (PurgeResponse.HTTP_STATUS_POST_SUCCESS != purgeResponse.getHttpStatus()) {
			throw new ClientFacadeProcessingException(MessageFormat.format("Response HTTP status was not successful {0}, reference [referenceId={1}]", purgeResponse, referenceId));
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("Successfully submitted purge request [purgeId={}, referenceId={}]", purgeResponse.getPurgeId(), referenceId);
		}
		
		if (waitForCompletion) {
			waitForCompletion(purgeResponse, maxWaitTime, referenceId);
		} else if (LOG.isDebugEnabled()) {
			LOG.debug("Not waiting for completion of purge request [purgeId={}, referenceId={}]", purgeResponse.getPurgeId(), referenceId);
		}
		
		return purgeResponse.getPurgeId();
	}

	private void waitForCompletion(PurgeResponse purgeResponse, int maxWaitTime, String referenceId) {
		boolean purgeComplete = false;
		int totalWaitingTime = 0;
		
		int waitTimeInSeconds = purgeResponse.getPingAfterSeconds();
		String progressUri = purgeResponse.getProgressUri();
		
		do {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Waiting for purge completion of purge request [purgeId={}, referenceId={}] for [{}] seconds", purgeResponse.getPurgeId(), referenceId, waitTimeInSeconds);
			}
			
			try {
				Thread.sleep(waitTimeInSeconds * 1000);
			} catch (InterruptedException e) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Wait for purge status check was interrupted");
				}
			}
			
			PurgeStatusRequest statusRequest = PurgeStatusRequest.builder()
					.progressUri(progressUri)
					.build();
			
			PurgeStatusResponse statusResponse = ccuClient.getPurgeStatus(statusRequest);
			if (PurgeStatusResponse.HTTP_STATUS_GET_SUCCESS != statusResponse.getHttpStatus()) {
				throw new ClientFacadeProcessingException(MessageFormat.format("Response HTTP status was not successful {0}, reference [referenceId={1}]", statusResponse, referenceId));
			}
			
			if (statusResponse.getPurgeStatus() == PurgeStatus.IN_PROGRESS) {
				totalWaitingTime += waitTimeInSeconds;
				if (totalWaitingTime >= maxWaitTime) {
					throw new ClientFacadeProcessingException(MessageFormat.format("Waiting for purge completion of purge request [purgeId={0}, referenceId={1}] exceeded maximum waiting time [{2}] seconds", purgeResponse.getPurgeId(), referenceId, maxWaitTime));
				}
				waitTimeInSeconds = statusResponse.getPingAfterSeconds();
				progressUri = statusResponse.getProgressUri();
			} else {
				purgeComplete = true;
			}
		} while (!purgeComplete);
	}

}
