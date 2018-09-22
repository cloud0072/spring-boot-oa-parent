package com.github.cloud0072.base.util;

import com.github.cloud0072.base.model.NamedEntity;
import com.github.cloud0072.common.util.ReflectUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 对象增强
 */
@Slf4j
public class EntityUtils {

    /**
     * 实体类缓存
     */
    private static final Map<String, Object> entityMap = new HashMap<>();

    /**
     * 无法实例化，保证只能使用静态方法
     */
    private EntityUtils() {
    }

    public static boolean isNull(Object o) {
        return o == null || "".equals(o);
    }

    public static <T> T orNull(T obj, T orNull) {
        return obj == null ? orNull : obj;
    }

    /**
     * 创建实例方法
     *
     * @param clazz
     * @param interfaceIndex
     * @param typeIndex
     * @return
     */
    public static Object interfaceGenericTypeInstance(Class<?> clazz, int interfaceIndex, int typeIndex) {
        return entityMap.computeIfAbsent(clazz.getName(), v -> {
            try {
                return ReflectUtils.getInterfaceGenericType(clazz, interfaceIndex, typeIndex).newInstance();
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });
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
