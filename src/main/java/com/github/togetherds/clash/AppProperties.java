package com.github.togetherds.clash;


import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

import java.util.Optional;

@ConfigMapping(prefix = "clash")
public interface AppProperties {
    @WithDefault("127.0.0.1")
    String host();

    @WithDefault("9090")
    Integer port();

    Optional<String> username();

    Optional<String> password();
}
