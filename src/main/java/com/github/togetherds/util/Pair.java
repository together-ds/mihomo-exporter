package com.github.togetherds.util;

import java.util.function.BiFunction;

public class Pair<T> {

    private final T latest;
    private final T prev;

    public Pair(T latest, T prev) {
        this.latest = latest;
        this.prev = prev;
    }

    public Pair<T> update(T newData) {
        return new Pair<>(newData, this.latest);
    }

    public T getLatest() {
        return latest;
    }


    public T getPrev() {
        return prev;
    }

    public <V> V map(BiFunction<T, T, V> f) {
        return f.apply(latest, prev);
    }
}
