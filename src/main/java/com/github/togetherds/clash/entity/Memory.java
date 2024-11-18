package com.github.togetherds.clash.entity;

public class Memory {
    private long inuse;
    private long oslimit;

    public long getOslimit() {
        return oslimit;
    }

    public void setOslimit(long oslimit) {
        this.oslimit = oslimit;
    }

    public long getInuse() {
        return inuse;
    }

    public void setInuse(long inuse) {
        this.inuse = inuse;
    }
}

