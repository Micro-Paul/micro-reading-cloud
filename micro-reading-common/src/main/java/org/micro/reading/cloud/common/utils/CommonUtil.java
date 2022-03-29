package org.micro.reading.cloud.common.utils;

import java.util.Collection;
import java.util.UUID;

/**
 * @author micro-paul
 * @date 2022年03月16日 9:30
 */
public class CommonUtil {

    /**
     * 判断集合是否为空
     *
     * @param collection
     * @return boolean
     * @author micro-paul
     * @date 2022/3/16 9:37
     */
    public static boolean isEmpty(Collection<?> collection) {

        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合不为空
     *
     * @param collection
     * @return boolean
     * @author micro-paul
     * @date 2022/3/16 9:39
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "");
    }


}
