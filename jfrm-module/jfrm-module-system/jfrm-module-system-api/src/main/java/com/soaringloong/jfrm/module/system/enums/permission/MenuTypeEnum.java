package com.soaringloong.jfrm.module.system.enums.permission;

/**
 * 菜单类型枚举类
 */
public enum MenuTypeEnum {

    DIR(1), // 目录
    MENU(2), // 菜单
    BUTTON(3) // 按钮
    ;

    /**
     * 类型
     */
    public final Integer type;

    MenuTypeEnum(Integer type) {
        this.type = type;
    }
}
