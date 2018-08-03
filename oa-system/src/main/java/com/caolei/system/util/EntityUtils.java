package com.caolei.system.util;

import com.caolei.system.api.BaseEntity;
import com.caolei.system.api.LoggerEntity;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 对象增强
 */
public class EntityUtils extends LoggerEntity {

    private static final Map<String, BaseEntity> entityMap = new HashMap<>();

    /**
     * 无法实例化，保证只能使用静态方法
     */
    private EntityUtils() {
    }

    public static BaseEntity interfaceGenericTypeInstance(Class<?> clazz, int interfaceIndex, int typeIndex) {
        return entityMap.computeIfAbsent(clazz.getName(), v -> {
            try {
                return (BaseEntity) ReflectUtils.getInterfaceGenericType(clazz, interfaceIndex, typeIndex).newInstance();
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });
    }

    public static boolean isNull(Object o) {
        return o == null || "".equals(o);
    }

    public static <T> T orNull(T obj, T orNull) {
        return obj == null ? orNull : obj;
    }

    /**
     * 转化为 checkbox列表
     *
     * @param collection
     * @param checked
     * @param <T>
     * @return
     */
    public static <T extends NamedEntity> List<Map> getCheckedList(Collection<T> collection, Collection<T> checked) {
        return collection.stream().map(t -> new HashMap<String, Object>() {{
            put("id", t.getId());
            put("name", t.getName());
            put("checked", checked.contains(t));
        }}).sorted(Comparator.comparing(m -> "checked").thenComparing(m -> "name")).collect(Collectors.toList());
    }

    /**
     * 带有名称的对象转换为 下拉框;
     *
     * @param collection
     * @param <T>
     * @return select 列表
     */
    public static <T extends NamedEntity> LinkedHashMap<String, String> getSelectMap(
            Collection<T> collection, LinkedHashMap<String, String> map) {
        collection.stream().sorted(Comparator.comparing(NamedEntity::getName)).forEach(t -> map.put(t.getId(), t.getName()));
        return map;
    }

    /**
     * @param collection
     * @param defaultTips
     * @param <T>
     * @return 返回附带提示的Map
     */
    public static <T extends NamedEntity> LinkedHashMap<String, String> getSelectMap(
            Collection<T> collection, String defaultTips) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>() {{
            put("", defaultTips);
        }};
        return getSelectMap(collection, map);
    }

}