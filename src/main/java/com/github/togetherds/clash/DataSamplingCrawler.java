package com.github.togetherds.clash;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.expositionformats.PrometheusTextFormatWriter;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.StreamingOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class DataSamplingCrawler {
    @Inject
    AppProperties appProperties;
    @Inject
    ObjectMapper objectMapper;
    public static final Logger LOGGER = LoggerFactory.getLogger(DataSamplingCrawler.class);
    PrometheusTextFormatWriter prometheusTextFormatWriter = new PrometheusTextFormatWriter(false);
    Counter eventsTotal;

    public void onShutdown(@Observes ShutdownEvent ev) {
        PrometheusRegistry.defaultRegistry.unregister(eventsTotal);
    }

    public void onStart(@Observes StartupEvent ev) {
        eventsTotal = Counter.builder()
                .name("events_total")
                .help("Total number of events")
                .register();
    }

    @Scheduled(every = "${scheduler.fetchInterval:5S}")
    public void counterIncrement() {
        eventsTotal.inc();
        LOGGER.info("Counter: {}", eventsTotal.get());
        LOGGER.info("objectMapper: {}", objectMapper);
    }

    public StreamingOutput getMetrics() {
        return output -> prometheusTextFormatWriter.write(output, PrometheusRegistry.defaultRegistry.scrape());
    }

}
