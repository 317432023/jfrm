package com.soaringloong.jfrm.framework.common.util;

import cn.hutool.core.lang.Assert;
import com.comm.pojo.SystemExceptionUtil;
import org.junit.jupiter.api.Test;

public class ServiceExceptionUtilTest {

	@Test
	public void doFormatTest() {
		String ret = SystemExceptionUtil.doFormat(400, "无效参数：{}，你好：{}", "ooo", "xxx");
		Assert.equals(ret, "无效参数：ooo，你好：xxx");
	}

}
