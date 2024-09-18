package com.soaringloong.jfrm.module.system.enums.permission;

/**
 * 角色标识枚举
 */
public enum RoleCodeEnum {

    SUPER_ADMIN("super_admin", "超级管理员"),
    TENANT_ADMIN("tenant_admin", "租户管理员"),
    CRM_ADMIN("crm_admin", "CRM 管理员"); // CRM 系统专用
    ;

    /**
     * 角色编码
     */
    public final String code;
    /**
     * 名字
     */
    public final String name;

    RoleCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static boolean isSuperAdmin(String code) {
        return SUPER_ADMIN.code.equals(code);
    }

}
