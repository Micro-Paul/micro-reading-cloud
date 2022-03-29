package org.micro.reading.cloud.common.enums;

/**
 * @author micro-paul
 * @date 2022年03月16日 17:50
 */
public enum BookSerialStatusEnum {

    SERIAL(1, "连载"),
    SUSPEND(2, "暂更"),
    END(3, "完结");

    private Integer value;
    private String name;

    BookSerialStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
