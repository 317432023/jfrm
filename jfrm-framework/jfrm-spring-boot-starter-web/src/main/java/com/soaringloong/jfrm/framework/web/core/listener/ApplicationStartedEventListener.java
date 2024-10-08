package com.soaringloong.jfrm.framework.web.core.listener;

import cn.hutool.core.net.NetUtil;
import org.jboss.logging.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

/**
 * 在log4j2加载之前读取 spring.application.name供log4j2引用，引用方式${ctx:appName}
 *
 */
public class ApplicationStartedEventListener implements GenericApplicationListener {

	public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 10;

	private static final Class<?>[] EVENT_TYPES = { ApplicationStartingEvent.class,
			ApplicationEnvironmentPreparedEvent.class, ApplicationPreparedEvent.class, ContextClosedEvent.class,
			ApplicationFailedEvent.class };

	private static final Class<?>[] SOURCE_TYPES = { SpringApplication.class, ApplicationContext.class };

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ApplicationEnvironmentPreparedEvent) {

			ConfigurableEnvironment envi = ((ApplicationEnvironmentPreparedEvent) event).getEnvironment();
			MutablePropertySources mps = envi.getPropertySources();

			PropertySource<?> ps = mps.get("configurationProperties");

			if (ps != null) {
				if (ps.containsProperty("spring.application.name")) {
					String appName = (String) ps.getProperty("spring.application.name");
					MDC.put("appName", appName);
				}

				String ip = NetUtil.getLocalhost().getHostAddress();
				if (StringUtils.hasLength(ip)) {
					MDC.put("ip", ip);
				}

				if (ps.containsProperty("server.port")) {
					Object port = ps.getProperty("server.port");
					if (port != null) {
						MDC.put("port", port.toString());
					}
				}
			}

		}
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see Ordered#getOrder()
	 */
	@Override
	public int getOrder() {
		return DEFAULT_ORDER;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see GenericApplicationListener#
	 * supportsEventType(org.springframework.core.ResolvableType)
	 */
	@Override
	public boolean supportsEventType(ResolvableType resolvableType) {
		return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
	}

	@Override
	public boolean supportsSourceType(Class<?> sourceType) {
		return isAssignableFrom(sourceType, SOURCE_TYPES);
	}

	private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
		if (type != null) {
			for (Class<?> supportedType : supportedTypes) {
				if (supportedType.isAssignableFrom(type)) {
					return true;
				}
			}
		}
		return false;
	}

}
