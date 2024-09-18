package com.soaringloong.jfrm.module.system.enums.permission;

import com.comm.pojo.core.IntArrayValuable;

import java.util.Arrays;

/**
 * 数据范围枚举类
 * <p>
 * 用于实现数据级别的权限
 */
public enum DataScopeEnum implements IntArrayValuable {

    ALL(1), // 全部数据权限

    DEPT_CUSTOM(2), // 指定部门数据权限
    DEPT_ONLY(3), // 部门数据权限
    DEPT_AND_CHILD(4), // 部门及以下数据权限

    SELF(5); // 仅本人数据权限

    /**
     * 范围
     */
    public final Integer scope;

    DataScopeEnum(Integer scope) {
        this.scope = scope;
    }

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(e -> e.scope).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
