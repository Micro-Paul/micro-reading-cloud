package org.micro.reading.cloud.common.enums;

/**
 * @author micro-paul
 * @date 2022年03月24日 17:04
 */
public enum BooklistMoreTypeEnum {

    /**
     *
     * @author micro-paul
     * @date 2022/3/24 17:07
     * @param null
     * @return null
     */
    NOTHING(1, "无"),
    MORE(2, "更多"),
    EXCHANGE(3, "换一换"),
    LOADING(4, "加载");

    private int value;

    private String name;

    BooklistMoreTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
