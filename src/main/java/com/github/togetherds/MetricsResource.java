package com.github.togetherds;

import com.github.togetherds.clash.DataSamplingCrawler;
import io.prometheus.metrics.expositionformats.PrometheusTextFormatWriter;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/metrics")
public class MetricsResource {
    @Inject
    DataSamplingCrawler dataSamplingCrawler;

    @GET
    @Produces(PrometheusTextFormatWriter.CONTENT_TYPE)
    public Response metrics() {
        return Response.ok().entity(dataSamplingCrawler.getMetrics()).build();
    }
}
