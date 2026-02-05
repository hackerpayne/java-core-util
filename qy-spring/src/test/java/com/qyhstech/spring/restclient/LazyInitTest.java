package com.qyhstech.spring.restclient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class LazyInitTest {

    static class TestClient extends QyBaseRestClient {
        boolean authConfigured;
        boolean configureAuthCalled;
        String apiKey;

        public TestClient(boolean lazyInit) {
            super(lazyInit);
            // Simulate some initialization that happens after super()
            this.apiKey = "test-key";
        }

        @Override
        protected void configureAuth(RestClient.Builder builder) {
            this.configureAuthCalled = true;
            // This should be called when buildRestClient is called
            if (this.apiKey != null) {
                this.authConfigured = true;
            }
        }
    }

    @Test
    public void testLazyInit() {
        TestClient client = new TestClient(true);

        // At this point, buildRestClient should NOT have been called
        Assertions.assertNull(client.getRestClient(), "RestClient should be null before init");
        Assertions.assertFalse(client.configureAuthCalled, "configureAuth should not be called yet");

        // Now initialize
        client.initRestClientIfNecessary();

        // Now it should be initialized
        Assertions.assertNotNull(client.getRestClient(), "RestClient should not be null after init");
        Assertions.assertTrue(client.configureAuthCalled, "configureAuth should be called after init");
        Assertions.assertTrue(client.authConfigured, "Auth should be configured with apiKey after init");
    }

    @Test
    public void testNormalInit() {
        TestClient client = new TestClient(false);

        // In normal init, it should be initialized immediately
        Assertions.assertNotNull(client.getRestClient(), "RestClient should not be null");
        Assertions.assertTrue(client.configureAuthCalled, "configureAuth should be called immediately");
        // Note: authConfigured will be false here because apiKey is not set when
        // configureAuth is called in the constructor
        Assertions.assertFalse(client.authConfigured,
                "Auth should NOT be configured with apiKey yet (demonstrating the problem with normal init)");
    }
}
