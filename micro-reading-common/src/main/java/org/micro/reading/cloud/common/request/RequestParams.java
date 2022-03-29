package org.micro.reading.cloud.common.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author micro-paul
 * @date 2022年03月16日 14:40
 */
public class RequestParams extends HashMap {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestParams.class);


    /**
     * 获取整形参数
     *
     * @param paramName
     * @return java.lang.Integer
     * @author micro-paul
     * @date 2022/3/16 14:41
     */
    public Integer getIntValue(String paramName) {
        Integer value = 0;
        Object o = get(paramName);
        if (Objects.isNull(o)) {
            return 0;
        }
        try {
            value = Integer.parseInt(o.toString());
        } catch (Exception e) {
            LOGGER.error("获取参数{}转换整型异常！{}", paramName, e);
            e.printStackTrace();
        }
        return value;
    }

    public String getStringValue(String paramName){
        String value = "";
        Object o = get(paramName);
        if (Objects.isNull(o)) {
            return value;
        }
        return o.toString();
    }
}
