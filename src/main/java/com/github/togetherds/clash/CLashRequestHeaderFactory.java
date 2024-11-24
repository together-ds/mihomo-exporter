package com.github.togetherds.clash;

import com.github.togetherds.util.Commons;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

@ApplicationScoped
public class CLashRequestHeaderFactory implements ClientHeadersFactory {
    @Inject
    private AppProperties appProperties;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientOutgoingHeaders) {
        appProperties.secret().ifPresent(secret -> {
            if (Commons.isNotEmpty(secret)) {
                clientOutgoingHeaders.add("Authorization", "Bearer " + secret);
            }
        });
        return clientOutgoingHeaders;
    }
}
