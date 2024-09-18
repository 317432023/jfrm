package com.soaringloong.jfrm.framework.web.core;

import com.soaringloong.jfrm.framework.web.core.listener.ApplicationStartedEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * spring-boot 启动主调方法 SpringBootMain
 *
 * @since 2023/1/16 23:47
 */
public final class SpringBootMain {

	public static void startup(Class<?> clazz, String[] args) {
		final String pidfile = args != null && args.length > 0 ? args[0] : null;

		SpringApplicationBuilder builder = new SpringApplicationBuilder(clazz)
			.initializers((ConfigurableApplicationContext context) -> {
				System.setProperty("serverId",
						Objects.requireNonNull(context.getEnvironment().getProperty("spring.application.name")));
			});
		SpringApplication app = builder.build(); // SpringApplication app = new
													// SpringApplication(clazz);
		if (StringUtils.hasLength(pidfile)) {
			app.addListeners(new ApplicationPidFileWriter(pidfile));
		}
		app.addListeners(new ApplicationStartedEventListener());
		ConfigurableApplicationContext ac = app.run(args); // SpringApplication.run(clazz,
															// args);
	}

}
