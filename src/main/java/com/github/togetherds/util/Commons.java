package com.github.togetherds.util;

import java.util.*;

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

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof String) {
            return isEmpty(((String) obj).isEmpty());
        }

        if (obj instanceof List) {
            return ((List<?>) obj).isEmpty();
        }

        if (obj instanceof Set) {
            return ((Set<?>) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }

        if (obj instanceof Optional<?>) {
            return ((Optional<?>) obj).isEmpty();
        }

        return false;
    }


}
