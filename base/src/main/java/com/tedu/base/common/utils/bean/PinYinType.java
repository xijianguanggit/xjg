package com.tedu.base.common.utils.bean;

/**
 * @author yangjixin
 * @Description: TODO
 * @date 2017/9/4
 */
public enum PinYinType {
    UPPER_CASE("UPPER_CASE", "全部大写"),
    LOW_CASE("LOW_CASE", "全部小写"),
    FIRST_UPPER_CASE("FIRST_UPPER_CASE", "首字母大写");

    private final String value;
    private final String description;

    private PinYinType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public static PinYinType get(String value) {
        for (PinYinType pinYinTypes : values()) {
            if (pinYinTypes.getValue().equals(value)) {
                return pinYinTypes;
            }
        }
        return null;
    }


    public String toString() {
        return this.value;
    }
}
