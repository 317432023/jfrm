package com.soaringloong.jfrm.framework.mybatis.core.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基础实体
 */
@Data
public abstract class BaseDO extends SuperBaseDO {

	/**
	 * 最后修改人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updater;

	/**
	 * 最后修改时间
	 */
	@JsonFormat(timezone = "GMT+8", locale = "zh_CN", pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY) // 设置为只读即仅序列化不接收反序列化(jackson)
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}
