package com.soaringloong.jfrm.server;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "接口示例")
@RestController
public class ExampleController {

	@Operation(summary = "示例接口", description = "取前10条用户")
	@GetMapping("example")
	public Object example(String str) {
		List<Map<String, Object>> list = SqlRunner.db().selectList("select * from system_users limit 10");
		list.forEach(e -> e.remove("password"));
		Assert.isTrue(str == null || str.equals(str.trim()), "WebMvcStringTrimAutoConfiguration not ok");
		return list.stream()
			.map(map -> BeanUtil.toBean(map, SystemUsers.class,
					CopyOptions.create().setFieldNameEditor(StrUtil::toCamelCase)))
			.collect(Collectors.toList());
	}

}
