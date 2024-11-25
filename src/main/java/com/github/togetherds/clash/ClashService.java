package com.github.togetherds.clash;

import com.github.togetherds.clash.entity.ConnectionResp;
import com.github.togetherds.clash.entity.GroupResp;
import com.github.togetherds.clash.entity.ProxiesResp;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

}
