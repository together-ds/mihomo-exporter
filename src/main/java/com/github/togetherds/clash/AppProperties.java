package com.github.togetherds.clash;


import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Optional;

@ConfigMapping(prefix = "clash")
public interface AppProperties {
    @WithDefault("http://127.0.0.1:9090")
    String url();

    Optional<String> secret();
}
