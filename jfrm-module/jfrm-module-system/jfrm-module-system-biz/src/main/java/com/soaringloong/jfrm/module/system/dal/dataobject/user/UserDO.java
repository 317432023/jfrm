package com.soaringloong.jfrm.module.system.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.soaringloong.jfrm.framework.mybatis.core.dataobject.TenantBaseUserDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.util.Set;

@TableName(value = "system_users", autoResultMap = true) // 由于 SQL Server 的 system_user 是关键字，所以使用 system_users
@KeySequence("system_users_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SystemUser对象", description = "系统用户")
public class UserDO extends TenantBaseUserDO {
    @Serial
    private static final long serialVersionUID = 5018768984758246188L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long deptId;

    /**
     * 岗位编号数组
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Set<Long> postIds;
}
