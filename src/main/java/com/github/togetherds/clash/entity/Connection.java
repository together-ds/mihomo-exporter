package com.github.togetherds.clash.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class Connection {
    private String id;
    private Metadata metadata;
    private long upload;
    private long download;
    private String start;
    private List<String> chains;
    private String rule;
    private String rulePayload;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public long getUpload() {
        return upload;
    }

    public void setUpload(long upload) {
        this.upload = upload;
    }

    public long getDownload() {
        return download;
    }

    public void setDownload(long download) {
        this.download = download;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public List<String> getChains() {
        return chains;
    }

    public void setChains(List<String> chains) {
        this.chains = chains;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getRulePayload() {
        return rulePayload;
    }

    public void setRulePayload(String rulePayload) {
        this.rulePayload = rulePayload;
    }
}
