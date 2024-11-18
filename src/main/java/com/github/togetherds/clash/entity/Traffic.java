package com.github.togetherds.clash.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Traffic {
    public long up;
    public long down;


    public long getUp() {
        return up;
    }

    public void setUp(long up) {
        this.up = up;
    }

    public long getDown() {
        return down;
    }

    public void setDown(long down) {
        this.down = down;
    }
}
