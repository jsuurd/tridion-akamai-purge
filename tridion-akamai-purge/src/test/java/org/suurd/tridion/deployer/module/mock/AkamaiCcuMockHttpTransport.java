package org.suurd.tridion.deployer.module.mock;

import java.io.IOException;

import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;

public class AkamaiCcuMockHttpTransport extends MockHttpTransport {

	public int lowLevelExecCalls;

	private int callsBeforeSuccess;

	public AkamaiCcuMockHttpTransport(int callsBeforeSuccess) {
		this.callsBeforeSuccess = callsBeforeSuccess;
	}

	@Override
	public LowLevelHttpRequest buildRequest(String method, String url) {
		LowLevelHttpRequest httpRequest = null;
		
		if (url.contains("/ccu/v2/queues")) {
			if (method.equals("GET")) {
				// Get current queue size
				httpRequest = new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {
						MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
						response.setStatusCode(200);
						response.setContent("{" + 
								" \"supportId\": \"17QY1405953107052757-292938848\"," + 
								" \"httpStatus\": 200," + 
								" \"detail\": \"The queue may take a minute to reflect new or removed requests.\"," + 
								" \"queueLength\": 4" + 
								"}");
						return response;
					}
				};
			} else {
				// Add a purge request
				httpRequest = new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {
						MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
						response.setStatusCode(201);
						response.setContent("{" + 
								" \"estimatedSeconds\": 5," + 
								" \"progressUri\": \"/ccu/v2/purges/57799d8b-10e4-11e4-9088-62ece60caaf0\"," + 
								" \"purgeId\": \"57799d8b-10e4-11e4-9088-62ece60caaf0\"," + 
								" \"supportId\": \"17PY1405953363409286-284546144\"," + 
								" \"httpStatus\": 201," + 
								" \"detail\": \"Request accepted.\"," + 
								" \"pingAfterSeconds\": 5" + 
								"}");
						return response;
					}
				};
			}
		} else if (url.contains("/ccu/v2/purges")) {
			// Get purge status
			lowLevelExecCalls++;
			if (lowLevelExecCalls <= callsBeforeSuccess) {
				httpRequest = new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {
						MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
						response.setStatusCode(201);
						response.setContent("{" + 
								" \"originalEstimatedSeconds\": 5," + 
								" \"originalQueueLength\": 0," + 
								" \"supportId\": \"17SY1405954814899441-292938848\"," + 
								" \"httpStatus\": 200," + 
								" \"progressUri\": \"/ccu/v2/purges/57799d8b-10e4-11e4-9088-62ece60caaf0\"," + 
								" \"purgeId\": \"57799d8b-10e4-11e4-9088-62ece60caaf0\"," + 
								" \"completionTime\": \"2014-07-21T14:42:18Z\"," + 
								" \"submittedBy\": \"client_name\"," + 
								" \"purgeStatus\": \"In-Progress\"," + 
								" \"submissionTime\": \"2014-07-21T14:39:30Z\"," + 
								" \"pingAfterSeconds\": 5" + 
								"}");
						return response;
					}
				};
			} else {
				httpRequest = new MockLowLevelHttpRequest() {
					@Override
					public LowLevelHttpResponse execute() throws IOException {
						MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
						response.setStatusCode(201);
						response.setContent("{" + 
								" \"originalEstimatedSeconds\": 5," + 
								" \"originalQueueLength\": 0," + 
								" \"supportId\": \"17SY1405954814899441-292938848\"," + 
								" \"httpStatus\": 200," +  
								" \"purgeId\": \"57799d8b-10e4-11e4-9088-62ece60caaf0\"," + 
								" \"completionTime\": \"2014-07-21T14:42:18Z\"," + 
								" \"submittedBy\": \"client_name\"," + 
								" \"purgeStatus\": \"Done\"," + 
								" \"submissionTime\": \"2014-07-21T14:39:30Z\"" + 
								"}");
						return response;
					}
				};
			}
		}
		
		return httpRequest;
	}

}
