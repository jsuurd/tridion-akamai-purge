package org.suurd.tridion.deployer.module.facade;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.suurd.akamai.ccu.client.model.Configuration;
import org.suurd.akamai.ccu.client.model.v2.Domain;
import org.suurd.akamai.ccu.client.provider.ConfigurationProvider;
import org.suurd.akamai.ccu.client.provider.HttpTransportProvider;
import org.suurd.tridion.deployer.module.mock.AkamaiCcuMockHttpTransport;

import com.google.api.client.http.HttpTransport;

public class AkamaiCcuV2ClientFacadeTests {

	private AkamaiCcuClientFacade akamaiCcuClientFacade;

	private String testPurgeUrl;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		Properties testProperties = new Properties();
		testProperties.load(getClass().getResourceAsStream("/testing.properties"));
		
		ConfigurationProvider configurationProvider = new ConfigurationProvider() {
			@Override
			public Configuration getConfiguration() {
				return Configuration.builder()
						.baseAuthority(testProperties.getProperty("baseAuthority"))
						.accessToken(testProperties.getProperty("accessToken"))
						.clientToken(testProperties.getProperty("clientToken"))
						.clientSecret(testProperties.getProperty("clientSecret"))
						.queuesEndpoint(testProperties.getProperty("queuesEndpoint"))
						.numberOfRetries(Integer.valueOf(testProperties.getProperty("numberOfRetries")))
						.build();
				
			}
		};
		
		// Switch to ApacheHttpTransportProvider for running test against Akamai CCU
		//HttpTransportProvider httpTransportProvider = new ApacheHttpTransportProvider();
		HttpTransportProvider httpTransportProvider = new HttpTransportProvider() {
			private final HttpTransport HTTP_TRANSPORT = new AkamaiCcuMockHttpTransport(3);
			@Override
			public HttpTransport getHttpTransport() {
				return HTTP_TRANSPORT;
			}
		};
		this.akamaiCcuClientFacade = new AkamaiCcuV2ClientFacade(configurationProvider, httpTransportProvider);
		this.testPurgeUrl = testProperties.getProperty("purgeUrl");
	}

	@Test
	public void invalidateArls_WithNotWaitingForCompletion_ShouldReturnValidPurgeId() {
		List<String> arls = new ArrayList<>();
		arls.add(testPurgeUrl);
		
		Domain domain = Domain.STAGING;
		boolean waitForCompletion = false;
		int maxWaitTime = 10;
		String referenceId = "tcm:0-100-66560";
		
		String purgeId = akamaiCcuClientFacade.invalidateArls(arls, domain, waitForCompletion, maxWaitTime, referenceId);
		
		assertNotNull(purgeId);
	}

	@Test
	public void invalidateArls_WithWaitingForCompletion_ShouldReturnValidPurgeId() {
		List<String> arls = new ArrayList<>();
		arls.add(testPurgeUrl);
		
		Domain domain = Domain.STAGING;
		boolean waitForCompletion = true;
		int maxWaitTime = 60;
		String referenceId = "tcm:0-100-66560";
		
		String purgeId = akamaiCcuClientFacade.invalidateArls(arls, domain, waitForCompletion, maxWaitTime, referenceId);
		
		assertNotNull(purgeId);
	}

	@Test
	public void invalidateArls_WithWaitingForCompletionTimesOut_ShouldThrowClientFacadeProcessingException() {
		List<String> arls = new ArrayList<>();
		arls.add(testPurgeUrl);
		
		Domain domain = Domain.STAGING;
		boolean waitForCompletion = true;
		int maxWaitTime = 10;
		String referenceId = "tcm:0-100-66560";
		
		thrown.expect(ClientFacadeProcessingException.class);
		akamaiCcuClientFacade.invalidateArls(arls, domain, waitForCompletion, maxWaitTime, referenceId);
	}

}
