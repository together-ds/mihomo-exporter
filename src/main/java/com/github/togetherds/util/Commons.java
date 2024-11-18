package com.github.togetherds.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Commons {
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    public static <T> Set<T> nullToEmpty(Set<T> set) {
        return set == null ? Collections.emptySet() : set;
    }

    public static <T> List<T> nullToEmpty(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    public static <T, P> Map<T, P> nullToEmpty(Map<T, P> map) {
        return map == null ? Collections.emptyMap() : map;
    }

}
