package com.soaringloong.jfrm.module.system.dal.dataobject.tenant;

import cn.hutool.core.annotation.PropIgnore;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soaringloong.jfrm.framework.mybatis.core.dataobject.BaseDO;
import com.soaringloong.jfrm.module.system.dal.dataobject.user.UserDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

@TableName(value = "system_tenant", autoResultMap = true)
@KeySequence("system_tenant_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SystemTenant对象", description = "系统租户")
public class TenantDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 4119982867134753362L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 租户名，唯一
     */
    private String name;
    /**
     * 联系人的用户编号
     * <p>
     * 关联 {@link UserDO#getUserId()}
     */
    private Long contactUserId;
    /**
     * 联系人
     */
    private String contactName;
    /**
     * 联系手机
     */
    private String contactMobile;

    /**
     * 租户状态
     * <p>
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 账号数量
     */
    private Integer accountCount;

    // 逻辑删除
    @JsonIgnore
    @PropIgnore
    @TableLogic(delval = "1", value = "0")
    private Boolean deleted;

}
