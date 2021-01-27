package com.jbs.ump.log.enums;


/**
 * @created by wjf
 * @date 2019/7/31 15:37
 * @description: 金额类型
 */

public enum DingDingSendUrlEnum {
    WARN(1, "报警群");

    /**
     * 编码
     */
    private int code;

    private String value;

    DingDingSendUrlEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
