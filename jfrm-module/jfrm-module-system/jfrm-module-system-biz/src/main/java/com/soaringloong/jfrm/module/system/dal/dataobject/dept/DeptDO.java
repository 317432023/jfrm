package com.soaringloong.jfrm.module.system.dal.dataobject.dept;

import cn.hutool.core.annotation.PropIgnore;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soaringloong.jfrm.framework.mybatis.core.dataobject.TenantBaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@TableName("system_dept")
@KeySequence("system_dept_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Schema(name = "SystemDept对象", description = "系统部门")
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptDO extends TenantBaseDO {
    @Serial
    private static final long serialVersionUID = 4669139945219650519L;

    public static final Long PARENT_ID_ROOT = 0L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "父部门id")
    private Long parentId;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "显示顺序")
    private Integer sort;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "状态(1-禁用,0-正常)")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // 设置为只读即仅序列化不接收反序列化(jackson)
    private Integer status;

    // 逻辑删除
    @JsonIgnore
    @PropIgnore
    @TableLogic(delval = "1", value = "0")
    private Boolean deleted;


}
