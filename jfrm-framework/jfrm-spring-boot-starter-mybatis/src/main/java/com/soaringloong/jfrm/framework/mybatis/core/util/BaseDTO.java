package com.soaringloong.jfrm.framework.mybatis.core.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用DTO查询参数
 *
 * @version 1.0
 */
@Setter
@Getter
@NoArgsConstructor
@Schema(name = "BaseDTO查询参数对象", description = "通用查询参数")
public class BaseDTO {

	/**
	 * 排序字段列表
	 */
	@Schema(description = "排序字段列表")
	List<String> sort = new ArrayList<>();

	/**
	 * 模糊查询文本（全字段模糊，去除静态和标记有@LikeIgnore注解）
	 */
	@Schema(description = "模糊查询文本")
	String text;

	/**
	 * 排序方式(true:asc, false:desc)
	 */
	@Schema(description = "排序方式")
	boolean asc;

}
