package com.github.togetherds.clash.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Map;

@RegisterForReflection
public class ProxiesResp {
    private Map<String, Proxy> proxies;

    public Map<String, Proxy> getProxies() {
        return proxies;
    }

    public void setProxies(Map<String, Proxy> proxies) {
        this.proxies = proxies;
    }
}
