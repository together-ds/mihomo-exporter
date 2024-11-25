package com.github.togetherds.clash;

import com.github.togetherds.clash.entity.*;
import com.github.togetherds.util.Pair;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.github.togetherds.util.Commons.nullToEmpty;

@ApplicationScoped
public class DataSamplingCrawler {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataSamplingCrawler.class);
    public static final String CLASH_GROUPS = "clash.groups";
    public static final String CLASH_PROXIES = "clash.proxies";
    public static final String CLASH_CONNECTIONS = "clash.connections";
    public static final String CLASH_TRAFFIC_CURRENT = "clash.traffic.current";
    public static final String CLASH_TRAFFIC_TOTAL = "clash.traffic.total";
    public static final String CLASH_MEMORY_INUSE = "clash.memory.inuse";

    @Inject
    AppProperties appProperties;

    @Inject
    MeterRegistry registry;

    ClashService clashService;

    private final AtomicReference<Pair<ConnectionResp>> latestConnectionResp = new AtomicReference<>(new Pair<>(new ConnectionResp(), new ConnectionResp()));
    private final AtomicReference<GroupResp> latestGroupResp = new AtomicReference<>(new GroupResp());
    private final AtomicReference<ProxiesResp> latestProxiesResp = new AtomicReference<>(new ProxiesResp());

    @PostConstruct
    public void init() {

        clashService = QuarkusRestClientBuilder.newBuilder()
            .baseUri(URI.create(appProperties.url()))
            .build(ClashService.class);

        // Create gauges that will dynamically fetch the latest values
        Gauge.builder(CLASH_TRAFFIC_CURRENT, latestConnectionResp, t -> {
                return t.get().map((latest, prev) -> {
                    long latestDownloadTotal = latest.getDownloadTotal();
                    long prevDownloadTotal = prev.getDownloadTotal();
                    if (latestDownloadTotal == 0 || prevDownloadTotal == 0) {
                        return 0d;
                    } else if (latestDownloadTotal < prevDownloadTotal) {
                        return 0d;
                    }
                    double seconds = latest.getTimestamp() - prev.getTimestamp() / 1000.0;
                    return (latestDownloadTotal - prevDownloadTotal) / seconds;
                });
            })
            .tag("direction", "download")
            .description("The current upload speed of Clash")
            .register(registry);

        Gauge.builder(CLASH_TRAFFIC_CURRENT, latestConnectionResp, t -> {
                return t.get().map((latest, prev) -> {
                    long latestUploadTotal = latest.getUploadTotal();
                    long prevUploadTotal = prev.getUploadTotal();
                    if (latestUploadTotal == 0 || prevUploadTotal == 0) {
                        return 0d;
                    } else if (latestUploadTotal < prevUploadTotal) {
                        return 0d;
                    }
                    double seconds = latest.getTimestamp() - prev.getTimestamp() / 1000.0;
                    return (latestUploadTotal - prevUploadTotal) / seconds;
                });
            })
            .tag("direction", "upload")
            .description("The current download speed of Clash")
            .register(registry);

        Gauge.builder(CLASH_TRAFFIC_TOTAL, latestConnectionResp, c -> c.get().getLatest().getUploadTotal())
            .tag("direction", "upload")
            .baseUnit(BaseUnits.BYTES)
            .description("Total upload bytes of Clash")
            .register(registry);

        Gauge.builder(CLASH_TRAFFIC_TOTAL, latestConnectionResp, c -> c.get().getLatest().getDownloadTotal())
            .tag("direction", "download")
            .baseUnit(BaseUnits.BYTES)
            .description("Total download bytes of Clash")
            .register(registry);

        Gauge.builder(CLASH_MEMORY_INUSE, latestConnectionResp, c -> c.get().getLatest().getMemory())
            .baseUnit(BaseUnits.BYTES)
            .description("Total memory used by Clash")
            .register(registry);

        Gauge.builder(CLASH_CONNECTIONS, latestConnectionResp, c -> nullToEmpty(c.get()
                .getLatest()
                .getConnections()).size())
            .tag("type", "total")
            .description("The total number of connections in Clash")
            .register(registry);

        Gauge.builder(CLASH_CONNECTIONS, latestConnectionResp, c -> nullToEmpty(c.get()
                .getLatest()
                .getConnections()).size())
            .tag("type", "total")
            .description("The total number of connections in Clash")
            .register(registry);

        Gauge.builder(CLASH_GROUPS, latestGroupResp, g -> nullToEmpty(g.get().getProxies()).size())
            .tag("type", "total")
            .description("The total number of connections in Clash")
            .register(registry);

        Gauge.builder(CLASH_GROUPS, latestGroupResp, g -> nullToEmpty(g.get().getProxies()).size())
            .tag("type", "total")
            .description("The total number of groups in Clash")
            .register(registry);

        Gauge.builder(CLASH_PROXIES, latestProxiesResp, g -> nullToEmpty(g.get().getProxies()).size())
            .tag("type", "total")
            .description("The total number of proxies in Clash")
            .register(registry);


        // For connection types, we'll update in the scheduled method
    }

    @Scheduled(every = "${scheduler.fetchInterval:5S}")
    public void updateMetrics() {

        try {
            ConnectionResp connections = clashService.connections();
            latestConnectionResp.set(latestConnectionResp.get().update(connections));

            List<Connection> connectionList = nullToEmpty(connections.getConnections());
            Map<String, List<Connection>> connectionTypeMap = connectionList.stream()
                .collect(Collectors.groupingBy(c -> c.getMetadata().getType()));

            connectionTypeMap.forEach((type, conns) -> {
                Gauge.builder(CLASH_CONNECTIONS, conns::size)
                    .tag("type", type)
                    .description("The number of " + type + " connections in Clash")
                    .register(registry);
            });
        } catch (Exception e) {
            LOGGER.warn("Failed to get connections:{}", e.getMessage());
        }

        try {
            GroupResp groupResp = clashService.group();
            this.latestGroupResp.set(groupResp);
            nullToEmpty(groupResp.getProxies()).stream()
                .collect(Collectors.groupingBy(Proxy::isAlive))
                .forEach((isAlive, proxies) -> {
                    String type = isAlive ? "alive" : "dead";
                    Gauge.builder(CLASH_GROUPS, proxies::size)
                        .tag("type", type)
                        .description("The number of " + type + " groups in Clash")
                        .register(registry);

                });
        } catch (Exception e) {
            LOGGER.warn("Failed to get group:{}", e.getMessage());
        }

        try {
            ProxiesResp proxiesResp = clashService.proxies();
            this.latestProxiesResp.set(proxiesResp);

            Map<String, Proxy> proxies = nullToEmpty(proxiesResp.getProxies());
            proxies.entrySet()
                .stream()
                .collect(Collectors.groupingBy(e -> e.getValue().isAlive()))
                .forEach((isAlive, proxyEntries) -> {
                    String type = isAlive ? "alive" : "dead";
                    Gauge.builder(CLASH_PROXIES, proxyEntries::size)
                        .tag("type", type)
                        .description("The number of " + type + " proxies in Clash")
                        .register(registry);
                });
        } catch (Exception e) {
            LOGGER.warn("Failed to get proxies:{}", e.getMessage());
        }


    }
}
