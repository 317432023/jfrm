package com.soaringloong.jfrm.module.system.dal.dataobject.perm;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.soaringloong.jfrm.framework.mybatis.core.dataobject.TenantBaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Set;

/**
 * 角色 DO
 */
@TableName(value = "system_role", autoResultMap = true)
@KeySequence("system_role_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "SystemRole对象", description = "角色")
public class RoleDO extends TenantBaseDO {

    @Serial
    private static final long serialVersionUID = 7908411430115279818L;

    @Schema(description = "主键")
    @TableId(value = "role_id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "角色代码(租户+角色代码 唯一)")
    private String code;

    @Schema(description = "角色名")
    private String name;

    @Schema(description = "显示顺序")
    private Integer sort;

    /**
     * 角色状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    @Schema(description = "是否禁用（0-启用,1-禁用）")
    private Integer status;

    /**
     * 角色类型
     * <p>
     * 枚举 {@link RoleTypeEnum}
     */
    @Schema(description = "角色类型（1-内置角色,2-自定义角色）")
    private Integer type;

    @Schema(description = "备注")
    private String remark;


    /**
     * 数据范围
     * <p>
     * 枚举 {@link DataScopeEnum}
     */
    @Schema(description = "数据范围（1-全部数据权限,2-指定部门数据权限,3-部门数据权限,4-部门及以下数据权限,5-仅本人数据权限）")
    private Integer dataScope;

    /**
     * 数据范围(指定部门数组)
     * <p>
     * 适用于 {@link #dataScope} 的值为 {@link DataScopeEnum#DEPT_CUSTOM} 时
     */
    @Schema(description = "数据范围(指定部门数组)")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Long> dataScopeDeptIds;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean deleted;
}