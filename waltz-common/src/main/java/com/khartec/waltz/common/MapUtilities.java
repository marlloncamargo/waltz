/*
 * Waltz - Enterprise Architecture
 * Copyright (C) 2016, 2017 Waltz open source project
 * See README.md for more information
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.khartec.waltz.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.khartec.waltz.common.Checks.checkNotNull;
import static com.khartec.waltz.common.ListUtilities.newArrayList;
import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;


public class MapUtilities {

    public static <K, V> HashMap<K, V> newHashMap() {
         return new HashMap<>();
    }


    public static <K, V> Map<K, V> newHashMap(K key, V val) {
        HashMap<K, V> map = newHashMap();
        map.put(key, val);
        return map;
    }


    public static <K, V> Map<K, V> newHashMap(K k1, V v1,
                                              K k2, V v2) {
        Map<K, V> map = newHashMap(k1, v1);
        map.put(k2, v2);
        return map;
    }


    public static <K, V> Map<K, V> newHashMap(K k1, V v1,
                                              K k2, V v2,
                                              K k3, V v3) {
        Map<K, V> map = newHashMap(k1, v1, k2, v2);
        map.put(k3, v3);
        return map;
    }



    public static <K, V> Map<K, V> newHashMap(K k1, V v1,
                                              K k2, V v2,
                                              K k3, V v3,
                                              K k4, V v4) {
        Map<K, V> map = newHashMap(k1, v1, k2, v2, k3, v3);
        map.put(k4, v4);
        return map;
    }


    public static <K, V> Map<K, V> newHashMap(K k1, V v1,
                                              K k2, V v2,
                                              K k3, V v3,
                                              K k4, V v4,
                                              K k5, V v5) {
        Map<K, V> map = newHashMap(k1, v1, k2, v2, k3, v3, k4, v4);
        map.put(k5, v5);
        return map;
    }


    public static <K, V> Map<K, V> newHashMap(K k1, V v1,
                                              K k2, V v2,
                                              K k3, V v3,
                                              K k4, V v4,
                                              K k5, V v5,
                                              K k6, V v6) {
        Map<K, V> map = newHashMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
        map.put(k6, v6);
        return map;
    }


    public static <K, V> Map<K, Collection<V>> groupBy(Function<V, K> keyFn,
                                                       Collection<V> xs) {
        return groupBy(keyFn, x -> x, xs);
    }

    public static <K, V, V2> Map<K, Collection<V2>> groupBy(Function<V, K> keyFn,
                                                            Function<V, V2> valueFn,
                                                            Collection<V> xs) {
        checkNotNull(xs, "xs cannot be null");
        checkNotNull(keyFn, "keyFn cannot be null");
        checkNotNull(valueFn, "valueFn cannot be null");

        Map<K, Collection<V2>> result = MapUtilities.newHashMap();
        for (V v: xs) {
            K key = keyFn.apply(v);
            Collection<V2> bucket = result.computeIfAbsent(key, u -> newArrayList());
            bucket.add(valueFn.apply(v));
            result.put(key, bucket);
        }
        return result;
    }


    public static <K, V> Map<K, V> indexBy(Function<V, K> keyFn,
                                           Collection<V> xs) {
        checkNotNull(xs, "xs cannot be null");
        checkNotNull(keyFn, "keyFn cannot be null");

        return indexBy(keyFn, identity(), xs);
    }


    public static <K, R, V> Map<K, R> indexBy(Function<V, K> keyFn,
                                              Function<V, R> valueFn,
                                              Collection<V> xs) {
        checkNotNull(xs, "xs cannot be null");
        checkNotNull(keyFn, "keyFn cannot be null");
        checkNotNull(valueFn, "valueFn cannot be null");

        return xs.stream()
                .collect(toMap(keyFn, valueFn));
    }


    public static <K, R, V> Map<K, R> indexBy(Function<V, K> keyFn,
                                              Function<V, R> valueFn,
                                              Collection<V> xs,
                                              BinaryOperator<R> mergeFunction) {
        checkNotNull(xs, "xs cannot be null");
        checkNotNull(keyFn, "keyFn cannot be null");
        checkNotNull(valueFn, "valueFn cannot be null");

        return xs.stream()
                .collect(toMap(keyFn, valueFn, mergeFunction));
    }


    public static <K, V> Map<K, Long> countBy(Function<V, K> keyFn,
                                              Collection<V> xs) {
        if (xs == null) {
            return emptyMap();
        }
        return xs.stream()
                .collect(Collectors.groupingBy(keyFn, Collectors.counting()));
    }


    public static <K, V> Map<K, V> ensureNotNull(Map<K, V> maybeMap) {
        return maybeMap == null
                ? newHashMap()
                : maybeMap;
    }


    /**
     * Returns true if map is null or empty
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }


    public static <K, V> Optional<V> maybeGet(Map<K, V> map,
                                              K key) {
        return map == null
                ? Optional.empty()
                : Optional.ofNullable(map.get(key));
    }

    /**
     * Similar to groupby, however the valueFn runs over the entire group after the initial grouping
     * has been performed
     * @param keyFn  - extracts/derives the grouping key
     * @param valueFn - function which transforms each group
     * @param xs - initial values
     * @param <K> - key type
     * @param <V> - (initial value type)
     * @param <V2> - resultant value type
     * @return
     */
    public static <K, V, V2> Map<K, V2> groupAndThen(Function<V, K> keyFn,
                                                     Function<Collection<V>, V2> valueFn,
                                                     Collection<V> xs) {
        checkNotNull(xs, "xs cannot be null");
        checkNotNull(keyFn, "keyFn cannot be null");
        checkNotNull(valueFn, "valueFn cannot be null");

        Map<K, V2> result = MapUtilities.newHashMap();
        Map<K, Collection<V>> step1 = groupBy(keyFn, xs);

        for (Map.Entry<K, Collection<V>> entry : step1.entrySet()) {

            K key = entry.getKey();
            Collection<V> group = entry.getValue();
            V2 transformedGroup = valueFn.apply(group);
            result.put(key, transformedGroup);
        }
        return result;
    }


    public static<K, K2, V> Map<K2, V> transformKeys(Map<K, V> original, Function<K, K2> transformation) {
        Map<K2, V> output = new HashMap<>();
        original.entrySet()
                .forEach(d -> output.put(transformation.apply(d.getKey()), d.getValue()));
        return output;
    }


    /**
     * Given two maps where `map1` (provides the 'domain') goes from K1 -> K2 and
     * `map2` (provides the 'range') goes from K2 -> V returns a new map which
     * joins map1 and map2 giving a result which goes directly
     * from K1 -> V
     * @param map1 the domain map
     * @param map2 the range map
     * @param <K1> the key of the domain
     * @param <K2> the value of the domain and the key of the range map
     * @param <V> the value of the range map
     * @return a map going from K1 -> V
     */
    public static <K1, K2, V> Map<K1, V> compose(Map<K1, K2> map1, Map<K2, V> map2) {
        Map<K1, V> result = new HashMap();
        map1.entrySet().forEach(e -> result.put(e.getKey(), map2.get(e.getValue())));
        return result;
    }
}
