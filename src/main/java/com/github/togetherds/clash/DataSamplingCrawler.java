package com.github.togetherds.clash;

import com.github.togetherds.clash.entity.Connection;
import com.github.togetherds.clash.entity.Connections;
import com.github.togetherds.clash.entity.Traffic;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.expositionformats.PrometheusTextFormatWriter;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.StreamingOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.github.togetherds.util.Commons.nullToEmpty;

@ApplicationScoped
public class DataSamplingCrawler {
    @Inject
    AppProperties appProperties;
    ClashService clashService;
    public static final Logger LOGGER = LoggerFactory.getLogger(DataSamplingCrawler.class);
    PrometheusTextFormatWriter prometheusTextFormatWriter = new PrometheusTextFormatWriter(false);
    Gauge currentTrafficGauge;
    Gauge totalTrafficGauge;
    Gauge memoryGauge;
    Gauge connectionGauge;


    @PostConstruct
    public void init() {
        clashService = QuarkusRestClientBuilder.newBuilder().baseUri(URI.create(appProperties.url())).build(ClashService.class);
    }

    @PostConstruct
    public void onStart() {
        currentTrafficGauge = Gauge.builder().name("clash.traffic.current").help("The current upload and download speed of Clash").labelNames("direction").register();
        totalTrafficGauge = Gauge.builder().name("clash.traffic.sum").help("Total upload and download bytes of Clash").labelNames("direction").register();
        memoryGauge = Gauge.builder().name("clash.memory.inuse").help("Total memory used by Clash").register();
        connectionGauge = Gauge.builder().name("clash.connections").labelNames("type").help("The Connections in Clash").register();
    }

    @PreDestroy
    public void onDestory() {
        PrometheusRegistry.defaultRegistry.unregister(currentTrafficGauge);
        PrometheusRegistry.defaultRegistry.unregister(totalTrafficGauge);
        PrometheusRegistry.defaultRegistry.unregister(memoryGauge);
        PrometheusRegistry.defaultRegistry.unregister(connectionGauge);
    }

    @Scheduled(every = "${scheduler.fetchInterval:5S}")
    public void counterIncrement() {
        try {
            Traffic traffic = clashService.traffic();
            currentTrafficGauge.labelValues("upload").set(traffic.getUp());
            currentTrafficGauge.labelValues("download").set(traffic.getDown());
        } catch (Exception e) {
            LOGGER.warn("Failed to get traffic:{}", e.getMessage());
        }

        try {
            Connections connections = clashService.connections();
            totalTrafficGauge.labelValues("upload").set(connections.getUploadTotal());
            totalTrafficGauge.labelValues("download").set(connections.getDownloadTotal());
            memoryGauge.set(connections.getMemory());
            List<Connection> connectionList = nullToEmpty(connections.getConnections());
            connectionGauge.clear();
            connectionGauge.labelValues("total").set(connectionList.size());
            Map<String, List<Connection>> collectionTypeMap = connectionList.stream().collect(Collectors.groupingBy(c -> c.getMetadata().getType()));
            collectionTypeMap.forEach((key, value) -> connectionGauge.labelValues(key).set(value.size()));
        } catch (Exception e) {
            LOGGER.warn("Failed to get connections:{}", e.getMessage());
        }
    }

    public StreamingOutput getMetrics() {
        return output -> prometheusTextFormatWriter.write(output, PrometheusRegistry.defaultRegistry.scrape());
    }


}
