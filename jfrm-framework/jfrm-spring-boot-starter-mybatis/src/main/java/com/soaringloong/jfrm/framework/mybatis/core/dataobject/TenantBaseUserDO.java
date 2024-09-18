package com.soaringloong.jfrm.framework.mybatis.core.dataobject;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 基础用户(区分租户)
 *
 * @since 2023/1/31 9:47
 */

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantBaseUserDO extends TenantBaseDO {

	/**
	 * 用户名
	 */
	@TableField(updateStrategy = FieldStrategy.NEVER)
	private String username;

	/**
	 * 密码
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 设置为只写即仅接收反序列化(jackson)
	private String password;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 用户性别
	 * <p>
	 * 枚举类 {@link com.soaringloong.jfrm.module.system.enums.common.SexEnum}
	 */
	private Integer sex;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 手机号码
	 */
	private String mobile;

	/**
	 * 状态(1-禁用,0-正常)
	 * <p>
	 * 枚举类 {@link com.comm.pojo.enums.CommonStatusEnum}
	 */
	@JsonProperty(access = JsonProperty.Access.READ_ONLY) // 设置为只读即仅序列化不接收反序列化(jackson)
	private Integer status;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 最后登录IP
	 */
	private String loginIp;

	/**
	 * 最后登录时间
	 */
	@JsonFormat(timezone = "GMT+8", locale = "zh_CN", pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime loginDate;

	/**
	 * 是否删除
	 */
	@TableLogic
	private Boolean deleted;

}
