package com.soaringloong.jfrm.framework.web.core.filter;

import cn.hutool.core.util.StrUtil;
import com.soaringloong.jfrm.framework.web.config.WebProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * 过滤 /admin-api、/member-api 等 API 请求的过滤器
 *
 */
@RequiredArgsConstructor
public abstract class ApiRequestFilter extends OncePerRequestFilter {

	protected final WebProperties webProperties;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		// 只过滤 API 请求的地址
		return !StrUtil.startWithAny(request.getRequestURI(), webProperties.getAdminApi().getPrefix(),
				webProperties.getMemberApi().getPrefix());
	}

}
