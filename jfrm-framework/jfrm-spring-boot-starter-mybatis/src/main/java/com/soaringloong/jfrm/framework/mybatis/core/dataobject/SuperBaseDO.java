package com.soaringloong.jfrm.framework.mybatis.core.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fhs.core.trans.vo.TransPojo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体
 * <p>
 * 使用 Easy-Trans TransType.SIMPLE 模式，集成 MyBatis Plus
 * 查询。<a href="https://www.cnblogs.com/Fooo/p/17847069.html">参考</a>
 */
@Data
// @Data 相当于
// @Getter@Setter@ToString@NoArgsConstructor@EqualsAndHashCode，如果在实体类中手动编写了一个带参数的构造方法，使用
// @Data 注解会覆盖掉手动编写的构造方法。
@JsonIgnoreProperties(value = "transMap") // 由于 Easy-Trans 会添加 transMap 属性，避免 Jackson 在
											// Spring Cache 反序列化报错
public abstract class SuperBaseDO implements Serializable, TransPojo {

	/**
	 * 创建人(制单人)
	 */
	@TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER) // 创建后不允许修改(mybatisplus)
	private String creator;

	/**
	 * 创建时间(制单时间)
	 */
	@JsonFormat(timezone = "GMT+8", locale = "zh_CN", pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY) // 设置为只读即仅序列化不接收反序列化(jackson)
	@TableField(fill = FieldFill.INSERT, updateStrategy = FieldStrategy.NEVER) // 初始插入时填充字段并且不可修改(mybatis-plus)
	private LocalDateTime createTime;

}
