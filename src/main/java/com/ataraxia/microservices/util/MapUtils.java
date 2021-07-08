package com.ataraxia.microservices.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Li Long
 * @Description 基于guava的可变集合工具类
 */
public class MapUtils {
    public static <K, V> HashMap<K, V> of() {
        return Maps.newHashMap();
    }

    public static <K, V> HashMap<K, V> of(K k1, V v1) {
        return Maps.newHashMap(ImmutableMap.of(k1, v1));
    }

    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2) {
        return Maps.newHashMap(ImmutableMap.of(k1, v1, k2, v2));
    }

    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return Maps.newHashMap(ImmutableMap.of(k1, v1, k2, v2, k3, v3));
    }

    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return Maps.newHashMap(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4));
    }

    public static <K, V> HashMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return Maps.newHashMap(ImmutableMap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
    }

    public static <K, V> HashMap<K, V> of(Map<K, V> hashMap) {
        return Maps.newHashMap(hashMap);
    }
}
