package com.github.togetherds.clash;

import com.github.togetherds.clash.entity.*;
import com.github.togetherds.util.JSON;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.jboss.resteasy.reactive.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Supplier;

@Path("/")
@RegisterClientHeaders(CLashRequestHeaderFactory.class)
public interface ClashService {

    Logger LOGGER = LoggerFactory.getLogger(ClashService.class);

    @GET
    @Path("/connections")
    ConnectionResp connections();

    @GET
    @Path("/group")
    GroupResp group();

    @GET
    @Path("/proxies")
    ProxiesResp proxies();

    @GET
    @Path("/traffic")
    @Produces(MediaType.APPLICATION_JSON)
    RestResponse<InputStream> traffic0();

    @GET
    @Path("/memory")
    @Produces(MediaType.APPLICATION_JSON)
    RestResponse<InputStream> memory0();

    default Memory memory() {
        String response = readFirstLine(this::memory0);
        if (response == null) {
            return null;
        }
        return JSON.toBean(response, Memory.class);
    }

    default Traffic traffic() {
        String response = readFirstLine(this::traffic0);
        if (response == null) {
            return null;
        }
        return JSON.toBean(response, Traffic.class);
    }

    private String readFirstLine(Supplier<RestResponse<InputStream>> responseSupplier) {
        try (RestResponse<InputStream> response = responseSupplier.get()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity()));
            return reader.readLine();
        } catch (IOException e) {
            LOGGER.warn("Failed to get traffic:{}", e.getMessage());
            return null;
        }
    }
}
