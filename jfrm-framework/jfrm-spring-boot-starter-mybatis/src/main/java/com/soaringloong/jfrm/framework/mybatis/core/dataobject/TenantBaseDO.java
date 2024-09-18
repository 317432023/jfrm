package com.soaringloong.jfrm.framework.mybatis.core.dataobject;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基础实体(区分租户)
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantBaseDO extends BaseDO {

	/**
	 * 租户 ID
	 */
	@TableField(updateStrategy = FieldStrategy.NEVER)
	private Long tenantId;

}
