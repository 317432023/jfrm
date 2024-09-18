package com.soaringloong.jfrm.module.system.enums.common;

/**
 * 性别的枚举值
 *
 * @author 芋道源码
 */
public enum SexEnum {

    /** 男 */
    MALE(1),
    /** 女 */
    FEMALE(2),
    /* 未知 */
    UNKNOWN(0);

    /**
     * 性别
     */
    public final Integer sex;

    SexEnum(Integer sex) {
        this.sex = sex;
    }


}
