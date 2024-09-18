package com.soaringloong.jfrm.module.system.enums.permission;

public enum RoleTypeEnum {

    /**
     * 内置角色
     */
    SYSTEM(1),
    /**
     * 自定义角色
     */
    CUSTOM(2);

    public final Integer type;

    RoleTypeEnum(Integer type) {
        this.type = type;
    }
}
