package com.github.togetherds.clash.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Metadata {
    private String network;
    private String type;
    private String sourceIP;
    private String destinationIP;
    private String sourceIPASN;
    private String destinationIPASN;
    private String sourcePort;
    private String destinationPort;
    private String inboundIP;
    private String inboundPort;
    private String inboundName;
    private String inboundUser;
    private String host;
    private String dnsMode;
    //    private int uid;
    private String process;
    private String processPath;
    private String specialProxy;
    private String specialRules;
    private String remoteDestination;
    //    private int dscp;
    private String sniffHost;

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getDestinationIP() {
        return destinationIP;
    }

    public void setDestinationIP(String destinationIP) {
        this.destinationIP = destinationIP;
    }

    public String getSourceIPASN() {
        return sourceIPASN;
    }

    public void setSourceIPASN(String sourceIPASN) {
        this.sourceIPASN = sourceIPASN;
    }

    public String getDestinationIPASN() {
        return destinationIPASN;
    }

    public void setDestinationIPASN(String destinationIPASN) {
        this.destinationIPASN = destinationIPASN;
    }

    public String getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(String sourcePort) {
        this.sourcePort = sourcePort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getInboundIP() {
        return inboundIP;
    }

    public void setInboundIP(String inboundIP) {
        this.inboundIP = inboundIP;
    }

    public String getInboundPort() {
        return inboundPort;
    }

    public void setInboundPort(String inboundPort) {
        this.inboundPort = inboundPort;
    }

    public String getInboundName() {
        return inboundName;
    }

    public void setInboundName(String inboundName) {
        this.inboundName = inboundName;
    }

    public String getInboundUser() {
        return inboundUser;
    }

    public void setInboundUser(String inboundUser) {
        this.inboundUser = inboundUser;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDnsMode() {
        return dnsMode;
    }

    public void setDnsMode(String dnsMode) {
        this.dnsMode = dnsMode;
    }


    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProcessPath() {
        return processPath;
    }

    public void setProcessPath(String processPath) {
        this.processPath = processPath;
    }

    public String getSpecialProxy() {
        return specialProxy;
    }

    public void setSpecialProxy(String specialProxy) {
        this.specialProxy = specialProxy;
    }

    public String getSpecialRules() {
        return specialRules;
    }

    public void setSpecialRules(String specialRules) {
        this.specialRules = specialRules;
    }

    public String getRemoteDestination() {
        return remoteDestination;
    }

    public void setRemoteDestination(String remoteDestination) {
        this.remoteDestination = remoteDestination;
    }

    public String getSniffHost() {
        return sniffHost;
    }

    public void setSniffHost(String sniffHost) {
        this.sniffHost = sniffHost;
    }
}
