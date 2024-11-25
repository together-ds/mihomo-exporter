package com.github.togetherds.clash.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class ConnectionResp {
    private long downloadTotal;
    private long uploadTotal;
    private List<Connection> connections;
    private long memory;
    private final long timestamp = System.currentTimeMillis();

    public long getDownloadTotal() {
        return downloadTotal;
    }

    public void setDownloadTotal(long downloadTotal) {
        this.downloadTotal = downloadTotal;
    }

    public long getUploadTotal() {
        return uploadTotal;
    }

    public void setUploadTotal(long uploadTotal) {
        this.uploadTotal = uploadTotal;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
