package org.suurd.tridion.deployer.module.configuration;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.suurd.akamai.ccu.client.model.ConfigurationBuilder;
import org.suurd.akamai.ccu.client.model.v2.Domain;
import org.suurd.akamai.ccu.client.provider.ConfigurationProvider;

import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;

public class AkamaiConfiguration implements ConfigurationProvider {

	private org.suurd.akamai.ccu.client.model.Configuration configuration;

	private Domain domain;

	public AkamaiConfiguration() {
		super();
	}

	public AkamaiConfiguration(Configuration config) throws ConfigurationException {
		super();
		
		Configuration akamaiConfig = config.getChild("AkamaiCcuClient");
		
		// Mandatory configuration
		ConfigurationBuilder configurationBuilder = org.suurd.akamai.ccu.client.model.Configuration.builder()
				.baseAuthority(akamaiConfig.getAttribute("BaseAuthority"))
				.accessToken(akamaiConfig.getAttribute("AccessToken"))
				.clientToken(akamaiConfig.getAttribute("ClientToken"))
				.clientSecret(akamaiConfig.getAttribute("ClientSecret"))
				.queuesEndpoint(akamaiConfig.getAttribute("QueuesEndpoint"));
		
		// Optional configuration
		if (akamaiConfig.hasAttribute("ConnectTimeout")) {
			configurationBuilder.connectTimeout(akamaiConfig.getAttributeAsInt("ConnectTimeout"));
		}
		if (akamaiConfig.hasAttribute("ReadTimeout")) {
			configurationBuilder.readTimeout(akamaiConfig.getAttributeAsInt("ReadTimeout"));
		}
		if (akamaiConfig.hasAttribute("NumberOfRetries")) {
			configurationBuilder.numberOfRetries(akamaiConfig.getAttributeAsInt("NumberOfRetries"));
		}
		
		this.configuration = configurationBuilder.build();
		
		if (akamaiConfig.hasAttribute("Domain")) {
			this.domain = Domain.valueOf(akamaiConfig.getAttribute("Domain").toUpperCase());
		}
	}

	@Override
	public org.suurd.akamai.ccu.client.model.Configuration getConfiguration() {
		return this.configuration;
	}

	public void setConfiguration(org.suurd.akamai.ccu.client.model.Configuration configuration) {
		this.configuration = configuration;
	}

	public Domain getDomain() {
		return this.domain;
	}

	public void setDomain(Domain ccuDomain) {
		this.domain = ccuDomain;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
	}

}
